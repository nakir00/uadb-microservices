import { Outlet, createFileRoute, redirect } from '@tanstack/react-router'
import Navbar from '@/components/auth/nav-bar'


export const Route = createFileRoute('/auth/_authLayout')({
    beforeLoad({context}) {
          if (context.auth.user){
              if(context.auth.user.role =="ROLE_LOCATAIRE"){
                  throw redirect({to:"/locataire"})
              }else{
                  throw redirect({to:"/proprietaire"})
              }
          }
      },
  component: RouteComponent,
})

function RouteComponent() {
  return <div className='flex flex-col justify-between h-screen max-h-screen max-w-full'>
    <Navbar />
    <div className='flex justify-center items-center h-full'>
        
        <Outlet />
    </div>

  </div>
}
