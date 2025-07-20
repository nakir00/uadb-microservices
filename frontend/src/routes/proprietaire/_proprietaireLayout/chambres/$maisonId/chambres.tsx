import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute(
  '/proprietaire/_proprietaireLayout/chambres/$maisonId/chambres',
)({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/proprietaire/_proprietaireLayout/chambres"!</div>
}
