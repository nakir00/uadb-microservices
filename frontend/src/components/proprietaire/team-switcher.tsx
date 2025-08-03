'use client'

import * as React from 'react'

import { RiAddLine, RiExpandUpDownLine } from '@remixicon/react'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'

export function TeamSwitcher({
  teams,
}: {
  teams: Array<{
    name: string
    logo: string
  }>
}) {
  const [activeTeam, setActiveTeam] = React.useState(teams[0] ?? null)

  if (!teams.length) return null

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <SidebarMenuButton
          size="lg"
          className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground gap-3 [&>svg]:size-auto"
        >
          <div className="flex aspect-square size-8 items-center justify-center rounded-md overflow-hidden bg-sidebar-primary text-sidebar-primary-foreground">
              <img
                src={activeTeam.logo}
                width={36}
                height={36}
                alt={activeTeam.name}
              />
          </div>
          <div className="grid flex-1 text-left text-base leading-tight">
            <span className="truncate font-medium">
              Proprietaire
            </span>
          </div>
          
        </SidebarMenuButton>
      </SidebarMenuItem>
    </SidebarMenu>
  )
}
