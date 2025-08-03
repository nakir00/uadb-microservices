import { Link } from '@tanstack/react-router'
import Logo from '@/components/navbar-components/logo'
import { Button } from '@/components/ui/button'
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from '@/components/ui/navigation-menu'
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover'

// Navigation links array to be used in both desktop and mobile menus
const navigationLinks = [
  { href: '#', label: 'Home', active: true },
  { href: '#', label: 'Features' },
  { href: '#', label: 'Pricing' },
  { href: '#', label: 'About' },
]

export default function Navbar() {
  return (
    <header className="border-b px-4 md:px-6">
      <div className="flex h-16 items-center justify-between gap-4">
        {/* Left side */}
        <div className="flex items-center gap-2">
          {/* Main nav */}
          <div className="flex items-center gap-6">
            <a href="#" className="text-primary hover:text-primary/90">
              <Logo />
            </a>
            {/* Navigation menu */}
            <NavigationMenu className="max-md:hidden">
              <NavigationMenuList className="gap-2">
                <Link to="/auth/login">
                  {({ isActive }) => {
                    return (
                      <>
                        <NavigationMenuItem>
                          <NavigationMenuLink
                            active={isActive}
                            href={'/auth/login'}
                            className="text-muted-foreground hover:text-primary py-1.5 font-medium"
                          >
                            login
                          </NavigationMenuLink>
                        </NavigationMenuItem>
                      </>
                    )
                  }}
                </Link>
                <Link to="/auth/register">
                  {({ isActive }) => {
                    return (
                      <>
                        <NavigationMenuItem>
                          <NavigationMenuLink
                            active={isActive}
                            href={'/auth/register'}
                            className="text-muted-foreground hover:text-primary py-1.5 font-medium"
                          >
                            creer un compte
                          </NavigationMenuLink>
                        </NavigationMenuItem>
                      </>
                    )
                  }}
                </Link>
              </NavigationMenuList>
            </NavigationMenu>
          </div>
        </div>
        {/* Right side */}
        <div className="flex items-center gap-2">
          
          <Button asChild size="sm" className="text-sm">
            <Link to=".." className=' flex flex-row '>
                  retour a la page d'accueil
            </Link>
          </Button>
        </div>
      </div>
    </header>
  )
}
