declare module "stompjs/lib/stomp" {
  export const Stomp: {
    over(socket: unknown): {
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
  };
}
