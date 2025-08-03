import { createFileRoute } from '@tanstack/react-router'
import HeroSection from '@/components/guest/home/hero-section'

export const Route = createFileRoute('/(visiteur)/_guestLayout/')({
  component: App,
})

function App() {
  return (
    <div className="text-center">
      <main>
        <HeroSection />
      </main>
    </div>
  )
}
