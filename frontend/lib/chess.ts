export const API = "http://localhost:8080";
export const WS = API + "/ws";
export const GAME_ID = "demo";

export function saveLogin(user: string, pass: string) {
  sessionStorage.setItem("chessUser", user);
  sessionStorage.setItem("chessPass", pass);
}

export function getUserColor() {
  if (typeof window === "undefined") return "WHITE";
  const user = sessionStorage.getItem("chessUser") || "white";
  return user.toUpperCase();
}

export function authHeaders(): Record<string, string> {
  if (typeof window === "undefined") return {};
  const user = sessionStorage.getItem("chessUser") || "";
  const pass = sessionStorage.getItem("chessPass") || "";
  if (!user || !pass) return {};
  return { Authorization: "Basic " + btoa(user + ":" + pass) };
}

const glyph: Record<string, string> = {
  King: "♚",
  Queen: "♛",
  Rook: "♜",
  Bishop: "♝",
  Knight: "♞",
  Pawn: "♟",
};

export function pieceGlyph(raw: string) {
  if (!raw) return { text: "", white: true };
  const parts = raw.split("_");
  const color = parts[0];
  const kind = parts[1];
  const g = glyph[kind] || "?";
  return { text: g, white: color === "W" };
}
