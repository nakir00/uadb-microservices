import {
  Calendar,
  ChevronDown,
  HandCoins,
  Handshake,
  Home,
  Inbox,
  LayoutDashboard,
  ScrollText,
  Search,
  Settings,
} from 'lucide-react'

import { Link } from '@tanstack/react-router'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '../ui/dropdown-menu'
import { NavUser } from './navuser'
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'

// Menu items.
const items = [
  {
    title: 'Home',
    url: '#',
    icon: Home,
  },
  {
    title: 'Inbox',
    url: '#',
    icon: Inbox,
  },
  {
    title: 'Calendar',
    url: '#',
    icon: Calendar,
  },
  {
    title: 'Search',
    url: '#',
    icon: Search,
  },
  {
    title: 'Settings',
    url: '#',
    icon: Settings,
  },
]

export function AppSidebar() {
  return (
    <Sidebar>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>locataire</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuItem>
                <Link to="/locataire/dashboard">
                  {({ isActive }) => {
                    return (
                      <SidebarMenuButton
                        asChild
                        className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                        isActive={isActive}
                      >
                        <div>
                          <LayoutDashboard
                            className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                            size={22}
                            aria-hidden="true"
                          />
                          <span>Accueil</span>
                        </div>
                      </SidebarMenuButton>
                    )
                  }}
                </Link>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <Link to="/locataire/rendez-vous">
                  {({ isActive }) => {
                    return (
                      <SidebarMenuButton
                        asChild
                        className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                        isActive={isActive}
                      >
                        <div>
                          <Handshake 
                            className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                            size={22}
                            aria-hidden="true"
                          />
                          <span>rendez-vous</span>
                        </div>
                      </SidebarMenuButton>
                    )
                  }}
                </Link>
              </SidebarMenuItem>
              <SidebarMenuItem>
                    <Link to="/locataire/contrats">
                      {({ isActive }) => {
                        return <SidebarMenuButton
                          asChild
                          className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                          isActive={isActive}
                        >
                          <div>
                              <ScrollText
                                className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                                size={22}
                                aria-hidden="true"
                              />
                            <span>Contrats</span>
                          </div>
                        </SidebarMenuButton>
                      }}
                    </Link>
              </SidebarMenuItem>
              {/* <SidebarMenuItem>
                    <Link to="/loca/paiements">
                      {({ isActive }) => {
                        return <SidebarMenuButton
                          asChild
                          className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                          isActive={isActive}
                        >
                          <div>
                              <HandCoins
                                className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                                size={22}
                                aria-hidden="true"
                              />
                            <span>Paiements</span>
                          </div>
                        </SidebarMenuButton>
                      }}
                    </Link>
                  </SidebarMenuItem> */}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <NavUser user={{ name: 'na', email: 'emai', avatar: 'ava' }} />
      </SidebarFooter>
    </Sidebar>
  )
}
