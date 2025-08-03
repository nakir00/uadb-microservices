import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/locataire/_locataireLayout/profile')(
  {
    component: RouteComponent,
  },
)

function RouteComponent() {
  return <div>Hello "/locataire/_locataireLayout/profile"!</div>
}
