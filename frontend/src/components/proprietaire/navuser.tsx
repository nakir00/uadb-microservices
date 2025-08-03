import { Expand, LogOut, User2Icon } from 'lucide-react'
import { Link, useNavigate  } from '@tanstack/react-router'
import { toast } from 'sonner'
import { Button } from '../ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'
import { useUser } from '@/hooks/user'

export function NavUser({
  user = { name: 'naby', email: 'email', avatar: 'avatar' },
}: {
  user: {
    name: string
    email: string
    avatar: string
  }
}) {

    const { logout } = useUser();
      const navigate = useNavigate();
    
      const logoutUser = () =>{
        logout(
          async (messagee) => {
             await navigate({ to: '/' })
            
          },
          (message) => {
            toast.error("une erreur s'est produite", {
              description: message,
            })
          },
        )
      }
  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <SidebarMenuButton
              size="lg"
              className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground [&>svg]:size-5"
            >
              <Avatar className="size-8">
                {/* <AvatarImage src={user.avatar} alt={user.name} /> */}
                <AvatarFallback className="rounded-lg">S</AvatarFallback>
              </Avatar>
              <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate font-medium">{user.name}</span>
              </div>
              <Expand className="ml-auto size-5 text-muted-foreground/80" />
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="w-(--radix-dropdown-menu-trigger-width) dark bg-sidebar"
            side="bottom"
            align="end"
            sideOffset={4}
          >
            <DropdownMenuGroup>
              <DropdownMenuItem className="gap-3 focus:bg-sidebar-accent">
                <Link to='/proprietaire/profile' className='w-full flex flex-start'>
                    <User2Icon
                        size={20}
                        className="size-5 text-muted-foreground/80"
                    />
                    Profile
                </Link>
                
              </DropdownMenuItem>

              <DropdownMenuItem
                asChild
                className="gap-3 focus:bg-sidebar-accent"
              >
                <Button variant={'ghost'} onClick={()=>logoutUser()} className='w-full flex flex-start'>
                  <LogOut
                    size={20}
                    className="size-5 text-muted-foreground/80"
                  />
                  Logout
                </Button>
              </DropdownMenuItem>
            </DropdownMenuGroup>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  )
}
