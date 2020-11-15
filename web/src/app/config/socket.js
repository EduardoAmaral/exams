/* eslint-disable @typescript-eslint/explicit-module-boundary-types */
import { RxStomp } from '@stomp/rx-stomp';
import SockJS from 'sockjs-client';
import { map } from 'rxjs/operators';

const url = '/ws';

const client = new RxStomp();

client.configure({
  brokerURL: url,
  reconnectDelay: 5000,
  heartbeatIncoming: 20000,
  heartbeatOutgoing: 20000,
  webSocketFactory: () => new SockJS(url),
});

client.activate();

export const questionCommentsSubscription = (id, callback) =>
  client
    .watch(`/question/${id}/comments`)
    .pipe(map((message) => JSON.parse(message.body)))
    .subscribe(callback);

export default client;
