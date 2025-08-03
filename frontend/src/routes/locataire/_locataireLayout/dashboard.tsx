import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/locataire/_locataireLayout/dashboard')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/proprietaire/proprietaireLayout/"!</div>
}
