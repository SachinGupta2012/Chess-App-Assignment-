"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { API, GAME_ID, saveLogin } from "../lib/chess";

export default function LoginPage() {
  const [user, setUser] = useState("white");
  const [pass, setPass] = useState("white123");
  const [tab, setTab] = useState("white");
  const [err, setErr] = useState("");
  const router = useRouter();

  function pick(which: string) {
    setTab(which);
    if (which === "white") {
      setUser("white");
      setPass("white123");
    } else {
      setUser("black");
      setPass("black123");
    }
  }

  async function doLogin() {
    setErr("");
    try {
      const res = await fetch(API + "/api/games/" + GAME_ID + "/board", {
        headers: { Authorization: "Basic " + btoa(user + ":" + pass) },
      });
      if (res.ok) {
        saveLogin(user, pass);
        router.push("/game");
      } else {
        setErr("Login failed. Use white/white123 or black/black123");
      }
    } catch {
      setErr("Backend not running on :8080 — start it first (mvn spring-boot:run)");
    }
  }

  return (
    <div className="login-wrap">
      <div className="card login-card">
        <div className="logo">♞</div>
        <h1>Chess</h1>
        <div className="sub">Play chess in two tabs with real-time moves</div>

        <div className="hint-row">
          <button
            type="button"
            className={"hint" + (tab === "white" ? " active" : "")}
            onClick={() => pick("white")}
          >
            <b>◯ Tab 1: White</b>
            white / white123
          </button>
          <button
            type="button"
            className={"hint" + (tab === "black" ? " active" : "")}
            onClick={() => pick("black")}
          >
            <b>● Tab 2: Black</b>
            black / black123
          </button>
        </div>

        <div className="field">
          <label>Username</label>
          <input className="input" value={user} onChange={(e) => setUser(e.target.value)} placeholder="username" />
        </div>
        <div className="field">
          <label>Password</label>
          <input className="input" type="password" value={pass} onChange={(e) => setPass(e.target.value)} placeholder="password" />
        </div>
        <button className="btn btn-login" onClick={doLogin}>Login →</button>
        {err && <div className="err">{err}</div>}
      </div>
    </div>
  );
}
