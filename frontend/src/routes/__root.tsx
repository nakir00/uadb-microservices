import { Outlet, createRootRouteWithContext } from '@tanstack/react-router'
import { TanStackRouterDevtools } from '@tanstack/react-router-devtools'
import { Toaster } from 'sonner'


import TanStackQueryLayout from '../integrations/tanstack-query/layout.tsx'

import type { QueryClient } from '@tanstack/react-query'
import type { useUserType } from '@/hooks/user.tsx'


interface MyRouterContext {

  queryClient: QueryClient,
  auth: ReturnType<useUserType>
}

export const Route = createRootRouteWithContext<MyRouterContext>()({
  component: () => (
    <>
      {/* <Header /> */}

      <Outlet />
      <TanStackRouterDevtools />

      <TanStackQueryLayout />
      <Toaster expand={true} richColors position="top-center"/>
    </>
  ),
})
