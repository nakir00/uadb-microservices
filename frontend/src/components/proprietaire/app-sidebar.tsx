import * as React from 'react'

import {
  RiBardLine,
  RiCodeSSlashLine,
  RiLayoutLeftLine,
  RiLeafLine,
  RiLoginCircleLine,
  RiLogoutBoxLine,
  RiScanLine,
  RiSettings3Line,
  RiUserFollowLine,
} from '@remixicon/react'
import { Link, useNavigate } from '@tanstack/react-router'
import { toast } from 'sonner'
import { HandCoins, Handshake, Home, LayoutDashboard, Newspaper, ScrollText, Settings } from 'lucide-react'
import { SearchForm } from '@/components/proprietaire/search-form'
import { TeamSwitcher } from '@/components/proprietaire/team-switcher'
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
  SidebarRail,
} from '@/components/ui/sidebar'
import { useUser } from '@/hooks/user'

// This is sample data.
const data = {
  teams: [
    {
      name: 'Proprietaire',
      logo: 'https://raw.githubusercontent.com/origin-space/origin-images/refs/heads/main/exp1/logo-01_kp2j8x.png',
    },
  ],
  navMain: [
    {
      title: 'Sections',
      url: '#',
      items: [
        {
          title: 'Dashboard',
          url: '#',
          icon: RiScanLine,
        },
        {
          title: 'Maisons',
          url: '#',
          icon: RiBardLine,
        },
      ],
    },
    {
      title: 'Other',
      url: '#',
      items: [
        {
          title: 'Settings',
          url: '#',
          icon: RiSettings3Line,
        },
      ],
    },
  ],
}

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  const { logout } = useUser()
  const navigate = useNavigate()

  const logoutUser = () => {
    logout(
      async (message) => {
        await navigate({ to: '/' })
        toast.info('vous avez été déconnecté', {
          description: message,
        })
      },
      (message) => {
        toast.error("une erreur s'est produite", {
          description: message,
        })
      },
    )
  }

  return (
    <Sidebar {...props}>
      <SidebarHeader>
        <TeamSwitcher teams={data.teams} />
        <hr className="border-t border-border mx-2 -mt-px" />
        
      </SidebarHeader>
      <SidebarContent>
        {/* We create a SidebarGroup for each parent. */}
          <SidebarGroup >
            <SidebarGroupLabel className="uppercase text-muted-foreground/60">
              chambre
            </SidebarGroupLabel>
            <SidebarGroupContent className="px-2">
              <SidebarMenu>
                  <SidebarMenuItem>
                    <Link to="/proprietaire/dashboard">
                      {({ isActive }) => {
                        return <SidebarMenuButton
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
                      }}
                    </Link>
                  </SidebarMenuItem>
                  <SidebarMenuItem>
                    <Link to="/proprietaire/maisons">
                      {({ isActive }) => {
                        return <SidebarMenuButton
                          asChild
                          className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                          isActive={isActive}
                        >
                          <div>
                              <Home
                                className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                                size={22}
                                aria-hidden="true"
                              />
                            <span>Maisons</span>
                          </div>
                        </SidebarMenuButton>
                      }}
                    </Link>
                    <Link to="/proprietaire/chambres">
                      {({ isActive }) => {
                        return <SidebarMenuButton
                          asChild
                          className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                          isActive={isActive}
                        >
                          <div>
                              <Home
                                className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                                size={22}
                                aria-hidden="true"
                              />
                            <span>Chambres</span>
                          </div>
                        </SidebarMenuButton>
                      }}
                    </Link>
                  </SidebarMenuItem>
                  
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
          <SidebarGroup >
            <SidebarGroupLabel className="uppercase text-muted-foreground/60">
              Contrats
            </SidebarGroupLabel>
            <SidebarGroupContent className="px-2">
              <SidebarMenu>
                  <SidebarMenuItem>
                    <Link to='/proprietaire/rendez-vous'>
                      {({ isActive }) => {
                        return <SidebarMenuButton
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
                      }}
                    </Link>
                  </SidebarMenuItem>
                  <SidebarMenuItem>
                    <Link to="/proprietaire/contrats">
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
                    <Link to="/proprietaire/paiements">
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
          <SidebarGroup >
            <SidebarGroupLabel className="uppercase text-muted-foreground/60">
              autres
            </SidebarGroupLabel>
            <SidebarGroupContent className="px-2">
              <SidebarMenu>
                  <SidebarMenuItem>
                    <Link to="/proprietaire/profile">
                      {({ isActive }) => {
                        return <SidebarMenuButton
                          asChild
                          className="group/menu-button font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
                          isActive={isActive}
                        >
                          <div>
                              <Settings
                                className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                                size={22}
                                aria-hidden="true"
                              />
                            <span>Profile</span>
                          </div>
                        </SidebarMenuButton>
                      }}
                    </Link>
                  </SidebarMenuItem>
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <hr className="border-t border-border mx-2 -mt-px" />
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton
              onClick={() => logoutUser()}
              className="font-medium gap-3 h-9 rounded-md bg-gradient-to-r hover:bg-transparent hover:from-sidebar-accent hover:to-sidebar-accent/40 data-[active=true]:from-primary/20 data-[active=true]:to-primary/5 [&>svg]:size-auto"
            >
              <RiLogoutBoxLine
                className="text-muted-foreground/60 group-data-[active=true]/menu-button:text-primary"
                size={22}
                aria-hidden="true"
              />
              <span>Se deconnecter</span>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarFooter>
      <SidebarRail />
    </Sidebar>
  )
}
