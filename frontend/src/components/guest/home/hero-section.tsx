import { Link } from '@tanstack/react-router'
import React from 'react'
import { buttonVariants } from '@/components/ui/button'
import { cn } from '@/lib/utils'

type Props = {}

const HeroSection = (props: Props) => {
  return (
    <section className="w-full h-screen md:h-[800px] mx-auto flex justify-center">
      <div className="relative w-full h-full md:h-[800px]">
        <img
          alt="Furniture"
          src="https://hiyori-backpack.s3.us-west-2.amazonaws.com/public/hero-image.jpg"
          width={1920}
          height={1200}
          className="h-full w-full object-cover "
        />
      </div>

      <div className="container absolute py-8 h-screen md:h-[800px] w-full">
        <div className="flex flex-col justify-center z-30 h-full">
          <p className="text-sm md:text-md uppercase tracking-widest text-white ">
            examen UADB 2023
          </p>
          <h1 className="text-5xl md:text-9xl font-bold text-white my-4 shadow-md">
            locaction de maisons
            <br />
            et chambres
          </h1>

          <div className='flex flex-row items-center justify-center'>
            <div className="flex space-x-4 mt-5 max-w-screen ">
            <Link
              to="/"
              className={cn(
                buttonVariants({ variant: "outline", size: "lg" }),
                "border-2 border-white text-black rounded px-8 py-3 ",
                "md:px-16 md:py-6",
                "hover:text-zinc-600 hover:bg-white",
              )}
            >
              voir nos locations
            </Link>

            {/* <div
              // href="https://github.com/clonglam/HIYORI-master"
              // target="_blank"
              className={cn(
                buttonVariants({ variant: "default", size: "lg" }),
                "border-2 border-primary text-white rounded px-8 py-3 ",
                "md:px-16 md:py-6",
              )}
            >
              View the Code
            </div> */}
          </div>
          </div>
        </div>
      </div>
    </section>
  )
}

export default HeroSection