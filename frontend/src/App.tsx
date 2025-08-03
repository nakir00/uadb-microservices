import { RouterProvider, createRouter } from '@tanstack/react-router'
import React from 'react'
import { routeTree } from './routeTree.gen'
import { UserContextProvider, useUser } from './hooks/user'

import * as TanStackQueryProvider from './integrations/tanstack-query/root-provider.tsx'


const router = createRouter({
  routeTree,
  context: {
    ...TanStackQueryProvider.getContext(),
    auth: undefined!,
  },
  defaultPreload: 'intent',
  scrollRestoration: true,
  defaultStructuralSharing: true,
  defaultPreloadStaleTime: 0,
})

// Register the router instance for type safety
declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}

type Props = {}

const App = (props: Props) => {
  const auth = useUser()
  return <RouterProvider router={router} context={{ auth: auth }} />
}

export default App
