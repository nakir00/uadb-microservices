import { createFileRoute, redirect } from '@tanstack/react-router'

export const Route = createFileRoute('/proprietaire/')({
    beforeLoad(ctx) {
        throw redirect({to:"/proprietaire/dashboard"})
    },
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/proprietaire/"!</div>
}
