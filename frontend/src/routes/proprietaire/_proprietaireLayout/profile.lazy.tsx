import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/proprietaire/_proprietaireLayout/profile')(
  {
    component: RouteComponent,
  },
)

function RouteComponent() {
  return <div>Hello "/proprietaire/_proprietaireLayout/profile"!</div>
}
