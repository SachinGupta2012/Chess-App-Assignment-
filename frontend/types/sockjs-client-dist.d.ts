declare module "sockjs-client/dist/sockjs" {
  type SockJsConstructor = new (url: string) => unknown;
  const SockJS: SockJsConstructor;
  export default SockJS;
}
