import { Link, Outlet, createFileRoute } from '@tanstack/react-router'
/* import { Link } from '@inertiajs/react'
import User from './user/user'
import Cart from './cart/cart'
import { CollectionModel } from '@/api/queries/collection'
import { SearchBarPopover } from './search_bar_popover'
import { useRef, useState } from 'react'

import { AlignRight, ShoppingBasket, User2 } from 'lucide-react' */
import { motion, useMotionValueEvent, useScroll } from 'framer-motion'
import { useRef, useState } from 'react'
import { AlignRight, ShoppingBasket, User2 } from 'lucide-react'
import { publish } from '@/lib/events'
import Logo from '@/components/navbar-components/logo'
import { NavigationMenu, NavigationMenuItem, NavigationMenuLink, NavigationMenuList } from '@/components/ui/navigation-menu'
import { useUser } from '@/hooks/user'

type Props = {}

const NavBar = (props: Props) => {
  const { user } = useUser()
  const [isHidden, setIsHidden] = useState(false)
  const [isOpenedSearchBar, setIsOpenedSearchBar] = useState(false)
  const { scrollY } = useScroll()
  const lastYRef = useRef(0)
  

  useMotionValueEvent(scrollY, 'change', (y) => {
    const difference = y - lastYRef.current
    if (Math.abs(difference) > 50 && !isOpenedSearchBar) {
      setIsHidden(difference > 0)
      lastYRef.current = y
    }
  })
  return (
    <motion.div
        animate={isHidden ? 'hidden' : 'visible'}
        whileHover="visible"
        onFocusCapture={() => setIsHidden(false)}
        variants={{
          hidden: {
            y: '-90%',
          },
          visible: {
            y: '0%',
          },
        }}
        transition={{ duration: 0.2 }}
        className={`grid grid-cols-1  lg:grid-cols-3 grid-rows-1 w-full h-[9vh] z-10 px-8  items-center bg-white  fixed top-0  justify-center  `}
      >
        {' '}
        {/* bg-gradient-to-b	 from-white to-transparent */}
        <div className={` justify-self-center lg:justify-self-start z-20`}>
          <Link to="/" className={` flex  flex-row items-center gap-4 font-SheAlwaysWalksAlone text-primary`}>
             <Logo />
            <span className="text-lg font-[700] font-palanquin">Examen maodo</span>
          </Link>
        </div>
        {/* <User path={path} /> */}
        <div className={'lg:flex flex-row justify-center gap-4 hidden '}>
          <NavigationMenu className="max-md:hidden">
              <NavigationMenuList className="gap-2">
                <Link to="/">
                  {({ isActive }) => {
                    return (
                      <>
                        <NavigationMenuItem>
                          <NavigationMenuLink
                            active={isActive}
                            className="text-muted-foreground hover:text-primary py-1.5 font-medium"
                          >
                            Accueil
                          </NavigationMenuLink>
                        </NavigationMenuItem>
                      </>
                    )
                  }}
                </Link>
                <Link to="/location">
                  {({ isActive }) => {
                    return (
                      <>
                        <NavigationMenuItem>
                          <NavigationMenuLink
                            active={isActive}
                            className="text-muted-foreground hover:text-primary py-1.5 font-medium"
                          >
                            Location
                          </NavigationMenuLink>
                        </NavigationMenuItem>
                      </>
                    )
                  }}
                </Link>
              </NavigationMenuList>
            </NavigationMenu>
        </div>
        <div className={'flex flex-row justify-end gap-3  '}>
          {/* <SearchBarPopover
            onOpened={(opened) => {
              setIsOpenedSearchBar(opened)
            }}
          /> */}
          <Link
            to={'/auth/login'}
            data-id={'user'}
            type="button"
            onClick={() => publish('open_user_sheet', {})}
            className={`z-10 lg:flex flex-row h-9 w-auto  hidden  items-center  justify-center transition-colors duration-100 focus-visible:outline-2  text-noir rounded-none `}
          >
            {user && <span>bienvenue {user.nomUtilisateur}</span>}
            <User2 className="w-6 h-6" />
          </Link>
        </div>
      </motion.div>
  )
}

export default NavBar