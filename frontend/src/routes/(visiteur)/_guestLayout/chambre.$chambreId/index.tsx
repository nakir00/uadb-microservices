import { Link, createFileRoute } from '@tanstack/react-router'
import type { MediaModel } from '@/api/queries/media'
import type { ChambreModel } from '@/api/queries/chambre'
import { api } from '@/api/api'
import { Badge } from '@/components/ui/badge'
import {
  Carousel,
  CarouselContent,
  CarouselIndicator,
  CarouselItem,
  CarouselNavigation,
} from '@/components/ui/carousel'
import { useUser } from '@/hooks/user'

export const Route = createFileRoute(
  '/(visiteur)/_guestLayout/chambre/$chambreId/',
)({
  loader(ctx) {
    const { chambreId } = ctx.params
    if (!chambreId) {
      throw new Error('Chambre ID is required')
    }
    return { chambreId: Number(chambreId) }
  },
  component: RouteComponent,
})

function RouteComponent() {
  const { chambreId } = Route.useLoaderData()
  // Here you can fetch the chambre details using the chambreId
  // For example, using a query hook from your API layer
  const { data: chambreData, isLoading } = api.chambre.AsGuestgetOne(chambreId)
  const { user } = useUser()

  if (isLoading) {
    return <div>Loading...</div>
  }
  const chambre = chambreData.data as ChambreModel

  return (
    <section className="py-8 bg-white md:py-16 dark:bg-gray-900 antialiased min-h-screen mt-20 flex items-center justify-center">
      <div className="max-w-screen-xl px-4 mx-auto 2xl:px-0">
        <div className="lg:grid lg:grid-cols-2 lg:gap-8 xl:gap-16">
          <div className="shrink-0 max-w-md lg:max-w-lg mx-auto">
            <div className="relative w-full  py-8">
              <div className="relative w-full px-4">
                <Carousel>
                  <CarouselContent>
                    {chambre.medias?.map((item: MediaModel) => {
                      return (
                        <CarouselItem key={item.id} className="p-4">
                          <div className="flex aspect-square items-center justify-center border border-zinc-200 dark:border-zinc-800">
                            {item.type === 'PHOTO' ? (
                              <img
                                className="w-full h-full object-cover"
                                src={item.url}
                                alt={item.description || 'Chambre Image'}
                              />
                            ) : (
                              <video
                                className="w-full h-full object-cover"
                                controls
                              >
                                <source src={item.url} type="video/mp4" />
                              </video>
                            )}
                          </div>
                        </CarouselItem>
                      )
                    })}
                  </CarouselContent>
                  <CarouselNavigation alwaysShow />
                  <CarouselIndicator />
                </Carousel>
              </div>
            </div>
          </div>

          <div className="mt-6 sm:mt-8 lg:mt-0">
            <h1 className="text-xl font-semibold text-gray-900 sm:text-2xl dark:text-white">
              {chambre.titre || 'Chambre Title'}
            </h1>
            <div className="mt-4 sm:items-center sm:gap-4 sm:flex">
              <p className="text-2xl font-extrabold text-gray-900 sm:text-3xl dark:text-white">
                {chambre.prix
                  ? `${chambre.prix} F.CFA / mois`
                  : 'Prix indisponible'}
              </p>
            </div>

            <div className="mt-6 sm:gap-4 sm:items-center sm:flex sm:mt-8">
              <Link
                to={user ? '/locataire/rendez-vous' : '/auth/login'}
                params={user ? { chambreId: chambre.id!.toString() } : {}}
                search={
                  user
                    ? {
                        chambreId: chambre.id!,
                      }
                    : {
                        redirect: `/locataire/rendez-vous`,
                        chambreId: chambre.id!,
                      }
                }
                className="text-white mt-4 sm:mt-0 bg-primary hover:ring-primary  focus:ring-4 focus:ring-primary font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-primary-600 dark:hover:bg-primary-700 focus:outline-none dark:focus:ring-primary-800 flex items-center justify-center"
                role="button"
              >
                <svg
                  className="w-5 h-5 -ms-2 me-2"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12.01 6.001C6.5 1 1 8 5.782 13.001L12.011 20l6.23-7C23 8 17.5 1 12.01 6.002Z"
                  />
                </svg>
                Prendre rendez-vous
              </Link>
            </div>

            <hr className="my-6 md:my-8 border-gray-200 dark:border-gray-800" />
            <h2 className="mb-4 text-gray-900 dark:text-white">
              Chambre Details
            </h2>
            <ul className="mb-6 text-gray-500 dark:text-gray-400">
              <li className="mb-2">
                <strong>Taille:</strong> {chambre.taille || 'N/A'} M²
              </li>
              <li className="mb-2">
                <strong>Type:</strong> <Badge>{chambre.type}</Badge>
              </li>
              <li className="mb-2">
                <strong>Meublée:</strong>{' '}
                {chambre.meublee ? <Badge>Oui</Badge> : <Badge>Non</Badge>}
              </li>
              <li className="mb-2">
                <strong>Salle de Bain:</strong>{' '}
                {chambre.salleDeBain ? <Badge>Oui</Badge> : <Badge>Non</Badge>}
              </li>
              <li className="mb-2">
                <strong>Disponible:</strong>{' '}
                {chambre.disponible ? <Badge>Oui</Badge> : <Badge>Non</Badge>}
              </li>
              {/* <li className="mb-2">
              <strong>Créé le:</strong> {new Date(chambre?.data.creeLe || '').toLocaleDateString('fr-FR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              }) || 'N/A'}
            </li> */}
            </ul>
            <hr className="my-6 md:my-8 border-gray-200 dark:border-gray-800" />
            <h2 className="mb-4 text-gray-900 dark:text-white">
              Maison Details
            </h2>
            <p className="text-gray-500 dark:text-gray-400">
              {chambre.maison?.description ||
                'No description available for this maison.'}
            </p>
            <ul className="mt-4 text-gray-500 dark:text-gray-400">
              <li className="mb-2">
                <strong>Adresse:</strong> {chambre.maison?.adresse || 'N/A'}
              </li>
              <li className="mb-2">
                <strong>Latitude:</strong>{' '}
                <Badge>{chambre.maison?.latitude || 'N/A'}</Badge>
              </li>
              <li className="mb-2">
                <strong>Longitude:</strong>{' '}
                <Badge>{chambre.maison?.longitude || 'N/A'}</Badge>
              </li>
              <li className="mb-2">
                <strong>Créé le:</strong>{' '}
                {new Date(chambre.maison?.creeLe || '').toLocaleDateString(
                  'fr-FR',
                  {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                  },
                ) || 'N/A'}
              </li>
            </ul>
            <hr className="my-6 md:my-8 border-gray-200 dark:border-gray-800" />
            <h2 className="mb-4 text-gray-900 dark:text-white">
              Propriétaire Details
            </h2>
            <p className="text-gray-500 dark:text-gray-400">
              {chambre.maison?.utilisateurDTO?.nomUtilisateur || 'N/A'}
            </p>
            <ul className="mt-4 text-gray-500 dark:text-gray-400">
              <li className="mb-2">
                <strong>Email:</strong>{' '}
                {chambre.maison?.utilisateurDTO?.email || 'N/A'}
              </li>
              <li className="mb-2">
                <strong>Téléphone:</strong>{' '}
                {chambre.maison?.utilisateurDTO?.telephone || 'N/A'}
              </li>
              <li className="mb-2">
                <strong>CNI:</strong>{' '}
                {chambre.maison?.utilisateurDTO?.CNI || 'N/A'}
              </li>

              <li className="mb-2">
                <strong>existe depuis :</strong>{' '}
                {new Date(
                  chambre.maison?.utilisateurDTO?.creeLe || '',
                ).toLocaleDateString('fr-FR', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                }) || 'N/A'}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  )
}
