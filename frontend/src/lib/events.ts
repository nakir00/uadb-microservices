// events.js

function subscribe(eventName: string, listener: EventListenerOrEventListenerObject): void {
  document.addEventListener(eventName, listener)
}

function unsubscribe(eventName: string, listener: EventListenerOrEventListenerObject): void {
  document.removeEventListener(eventName, listener)
}

function publish(eventName: string, data: any): void {
  const event = new CustomEvent(eventName, { detail: data })
  document.dispatchEvent(event)
}

export { publish, subscribe, unsubscribe }
