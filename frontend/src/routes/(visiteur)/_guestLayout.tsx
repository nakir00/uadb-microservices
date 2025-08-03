import { Outlet, createFileRoute } from '@tanstack/react-router'
import Footer from '@/components/guest/layout/footer'
import NavBar from '@/components/guest/layout/nav-bar'

export const Route = createFileRoute('/(visiteur)/_guestLayout')({
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <div className="w-screen ">
      <div className="relative w-full h-full font-Palanquin">
        {/* <div className="fixed top-0 z-10 w-full">
                  </div> */}
        <NavBar /*  url={url} headerCollections={headerCollections}  */ />
        {/* <main className="z-0 flex flex-col w-full h-full gap-6 overflow-y-auto bg-beige">  */}
        {/* snap-y snap-proximity scroll-smooth no-scrollbar */}
        {<Outlet />}
        {/* </main> */}
        <Footer />
        {/* <div className="fixed bottom-0 z-10 w-full md:hidden">
                  </div> */}
      </div>
    </div>
  )
}
