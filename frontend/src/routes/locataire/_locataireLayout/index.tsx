import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/locataire/_locataireLayout/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/locataire/locataire/"!</div>
}
