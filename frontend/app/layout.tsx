import "./globals.css";
import type { ReactNode } from "react";

export const metadata = { title: "Chess" };

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en">
      <body>
        <div className="topbar">
          <div className="brand">
            <div className="icon">♞</div>
            <span>Chess</span>
          </div>
        </div>
        {children}
      </body>
    </html>
  );
}
