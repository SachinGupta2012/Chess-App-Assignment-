"use client";
import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client/dist/sockjs";
import { Stomp } from "stompjs/lib/stomp";
import { API, GAME_ID, WS, authHeaders, getUserColor, pieceGlyph } from "../../lib/chess";

type MovePayload = {
  gameId: string;
  fromRow: number;
  fromCol: number;
  toRow: number;
  toCol: number;
  type: "MOVE";
  color: string;
  promotionPiece?: string;
};

type StompClient = {
  debug: (message: string) => void;
  connect: (
    headers: Record<string, string>,
    onConnect: () => void,
    onError: () => void
  ) => void;
  subscribe: (topic: string, callback: (message: { body: string }) => void) => void;
  send: (destination: string, headers: Record<string, string>, body: string) => void;
  disconnect: (callback: () => void) => void;
};

export default function GamePage() {
  const [board, setBoard] = useState<string[][]>([]);
  const [moves, setMoves] = useState<string[]>([]);
  const [turn, setTurn] = useState("WHITE");
  const [status, setStatus] = useState("IN_PROGRESS");
  const [sel, setSel] = useState<{ r: number; c: number } | null>(null);
  const [last, setLast] = useState<{ r: number; c: number } | null>(null);
  const [err, setErr] = useState("");
  const [connected, setConnected] = useState(false);
  const [histVisible, setHistVisible] = useState(true);
  const [promo, setPromo] = useState<{ fromRow: number; fromCol: number; toRow: number; toCol: number } | null>(null);
  const client = useRef<StompClient | null>(null);
  const [myColor, setMyColor] = useState("WHITE");

  function load() {
    fetch(API + "/api/games/" + GAME_ID + "/board", { headers: authHeaders() })
      .then((r) => {
        if (!r.ok) throw new Error("login");
        return r.json();
      })
      .then((d) => {
        setBoard(d.board);
        setMoves(d.moves || []);
        setTurn(d.turn);
        if (d.status) setStatus(d.status);
        setErr("");
      })
      .catch(() => {
        setErr("Backend offline or login expired. Go back and login again.");
        setConnected(false);
      });
  }

  function join() {
    if (client.current) {
      try { client.current.disconnect(() => {}); } catch {}
    }
    const st: StompClient = Stomp.over(new SockJS(WS));
    st.debug = () => {};
    st.connect(
      {},
      () => {
        setConnected(true);
        setErr("");
        st.subscribe("/topic/game." + GAME_ID, (m) => {
          const d = JSON.parse(m.body);
          if (d.type === "GAME") {
            if (d.board) setBoard(d.board);
            setMoves(d.moves || []);
            setTurn(d.turn);
            if (d.status) setStatus(d.status);
          }
          if (d.type === "MOVE") {
            setLast({ r: d.toRow, c: d.toCol });
          }
          if (d.type === "ERROR") {
            setErr(d.error);
          }
        });
        st.send("/app/game.join", {}, JSON.stringify({ gameId: GAME_ID }));
      },
      () => {
        setConnected(false);
        setErr("WebSocket disconnected. Click Join to reconnect.");
      }
    );
    client.current = st;
    load();
  }

  useEffect(() => {
    setMyColor(getUserColor());
    join();
    return () => {
      if (client.current) {
        try { client.current.disconnect(() => {}); } catch {}
      }
    };
    // eslint-disable-next-line
  }, []);

  function sendMove(fR: number, fC: number, tR: number, tC: number, promotion?: string) {
    const msg: MovePayload = { gameId: GAME_ID, fromRow: fR, fromCol: fC, toRow: tR, toCol: tC, type: "MOVE", color: myColor };
    if (promotion) msg.promotionPiece = promotion;
    client.current?.send("/app/game.move", {}, JSON.stringify(msg));
  }

  function click(r: number, c: number) {
    if (status === "CHECKMATE" || status === "STALEMATE" || status === "DRAW") {
      setErr("Game is over");
      return;
    }
    if (turn !== myColor) {
      setErr("Not your turn — waiting for " + turn);
      return;
    }
    const v = board[7 - r]?.[c] || "";
    const pieceColor = v.startsWith("W") ? "WHITE" : "BLACK";
    if (!sel) {
      if (!v) return;
      if (pieceColor !== myColor) {
        setErr("You can only move your own pieces (" + myColor + ")");
        return;
      }
      setErr("");
      setSel({ r, c });
      return;
    }
    if (sel.r === r && sel.c === c) {
      setSel(null);
      return;
    }
    const movingPiece = board[7 - sel.r]?.[sel.c] || "";
    const isPawn = movingPiece.endsWith("Pawn");
    const isLastRank = (myColor === "WHITE" && r === 7) || (myColor === "BLACK" && r === 0);
    if (isPawn && isLastRank) {
      setPromo({ fromRow: sel.r, fromCol: sel.c, toRow: r, toCol: c });
      setSel(null);
      return;
    }
    sendMove(sel.r, sel.c, r, c);
    setLast({ r, c });
    setSel(null);
  }

  function pickPromo(piece: string) {
    if (!promo) return;
    sendMove(promo.fromRow, promo.fromCol, promo.toRow, promo.toCol, piece);
    setLast({ r: promo.toRow, c: promo.toCol });
    setPromo(null);
  }

  function undo() {
    client.current?.send("/app/game.undo", {}, JSON.stringify({ gameId: GAME_ID, color: myColor }));
  }

  function plain(m: string) {
    return m.replace(/^\d+\.\s*/, "");
  }

  const ranks = [8, 7, 6, 5, 4, 3, 2, 1];
  const files = ["a", "b", "c", "d", "e", "f", "g", "h"];
  const gameOver = status === "CHECKMATE" || status === "STALEMATE" || status === "DRAW";
  const myTurn = turn === myColor && !gameOver;

  return (
    <div className="game-wrap">
      <div>
        <div className="controls">
          <button className="btn" onClick={join}>Reconnect</button>
          <button className="btn ghost" onClick={undo}>Undo</button>
          <span className="turn-pill">
            <span className={"turn-dot " + (turn === "WHITE" ? "w" : "b")} />
            Turn: {turn}
          </span>
          <span className={"you-pill " + (myTurn ? "on" : "")}>
            You: {myColor} {myTurn ? "· your move" : "· waiting"}
          </span>
          {gameOver && <span className="over-pill">{status}</span>}
        </div>

        {err && <div className="err-box">{err}</div>}

        <div className="board-shell">
          <div className="rank-col">
            {ranks.map((n) => (
              <div className="lab" key={n}>{n}</div>
            ))}
          </div>
          <div>
            <div className="board-frame">
              {board.map((row, i) => (
                <div className="brow" key={i}>
                  {row.map((v, j) => {
                    const r = 7 - i;
                    const c = j;
                    const g = pieceGlyph(v);
                    const isSel = sel && sel.r === r && sel.c === c;
                    const isLast = last && last.r === r && last.c === c;
                    const own = v && ((myColor === "WHITE" && v.startsWith("W")) || (myColor === "BLACK" && v.startsWith("B")));
                    return (
                      <div
                        key={j}
                        onClick={() => click(r, c)}
                        className={
                          "sq " + ((r + c) % 2 === 0 ? "dark" : "light") +
                          (isSel ? " sel" : "") + (isLast && !isSel ? " last" : "") +
                          (own && myTurn ? " can" : "")
                        }
                      >
                        <span className={"pc " + (g.white ? "w" : "b")}>{g.text}</span>
                      </div>
                    );
                  })}
                </div>
              ))}
            </div>
            <div className="file-row">
              {files.map((f) => (
                <div className="lab" key={f}>{f}</div>
              ))}
            </div>
          </div>
        </div>
        <div className="status-line">
          <span className={"dot " + (connected ? "on" : "off")} />
          {connected ? "Connected" : "Disconnected"} · You are {myColor} · Select your piece, then destination
        </div>
      </div>

      <div className="card hist">
        <div className="hist-head">
          <h3>🕐 Move History</h3>
          <button className="btn ghost" style={{ padding: "6px 12px" }} onClick={() => setHistVisible(!histVisible)}>
            {histVisible ? "Hide" : "Show"}
          </button>
        </div>
        {histVisible ? (
          <table>
            <thead>
              <tr><th>#</th><th>White</th><th>Black</th></tr>
            </thead>
            <tbody>
              {(() => {
                const rows: JSX.Element[] = [];
                for (let i = 0; i < moves.length; i += 2) {
                  rows.push(
                    <tr key={i}>
                      <td>{i / 2 + 1}</td>
                      <td>{plain(moves[i] || "")}</td>
                      <td>{plain(moves[i + 1] || "")}</td>
                    </tr>
                  );
                }
                return rows;
              })()}
            </tbody>
          </table>
        ) : null}
        <div className="note">
          Open one tab as white and one tab as black. Moves update in both tabs.
        </div>
      </div>

      {promo && (
        <div className="promo-overlay">
          <div className="promo-box">
            <h3>Promote pawn to:</h3>
            <div className="promo-btns">
              <button className="btn" onClick={() => pickPromo("QUEEN")}>Queen</button>
              <button className="btn" onClick={() => pickPromo("ROOK")}>Rook</button>
              <button className="btn" onClick={() => pickPromo("BISHOP")}>Bishop</button>
              <button className="btn" onClick={() => pickPromo("KNIGHT")}>Knight</button>
            </div>
            <button className="btn ghost" onClick={() => setPromo(null)}>Cancel</button>
          </div>
        </div>
      )}
    </div>
  );
}
