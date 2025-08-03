import { createFileRoute, redirect, useSearch } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import { z } from 'zod'
import type { RendezVousModel } from '@/api/queries/rendez-vous'
import { api } from '@/api/api'
import { NormalDataTable } from '@/blocs/proprietaire/table/tables/datatable'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'
import { subscribe, unsubscribe } from '@/lib/events'
import { useUser } from '@/hooks/user'
import { RendezVousColumns } from '@/blocs/locataire/table/columns/rendez-vous-columns'
import CreateRendezVousButton from '@/components/locataire/rendez-vous/create-rendez-vous-button'
import type { ContratModel } from '@/api/queries/contrat'

const productSearchSchema = z.object({
  chambreId: z.number().positive().optional()
})

export const Route = createFileRoute('/proprietaire/_proprietaireLayout/paiements/')({
  validateSearch: productSearchSchema,
  component: RouteComponent,
  loader({ context }) {
      if (!context.auth.user) {
        throw redirect({ to: '/auth/login', from: '/proprietaire/maisons' })
      }
      return { proprietaireId: context.auth.user.id }
    },
})

function RouteComponent() {
  // const { proprietaireId } = Route.useLoaderData()
  const { user } = useUser()
  const { chambreId } = Route.useSearch()
  const maisonCreate = api.maison.create()
  const [sortBy, setSortBy] = useState <keyof ContratModel> ('id')
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  // const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc')
  const [statut, setStatut] = useState< "EN_ATTENTE" | "CONFIRME" | "ANNULE" | undefined>('CONFIRME')
  const [search, setSearch] = useState<string | undefined>('')
  
  const { isPending, data, refetch } = api.rendezVous.getAll({
    page,
    size,
    statut: statut,
    // sort: [[sortBy, sortOrder, 'ignorecase']],
    proprietaireId: user!.id,
    maisonNom: search,
  })

  useEffect(() => {
    subscribe('refresh_proprietaire_rendez_vous_table', (e) => {
      refetch()
    })

    return () => {
      unsubscribe('refresh_proprietaire_rendez_vous_table', () => {})
    }
  }, [])

  return (
    <div className="flex flex-col gap-4 lg:gap-6 py-4 lg:py-6 ">
      {/* Page intro */}
      <div className="flex items-center justify-between gap-4">
        <div className="space-y-1">
          <h1 className="text-2xl font-semibold">Rendez-Vous</h1>
          <p className="text-sm text-muted-foreground">
            liste des rendez vous que vous avez
          </p>
        </div>
        
       {user && user.id && chambreId &&( <CreateRendezVousButton locataireId={user.id} chambreId={chambreId} />)}
      </div>

      <div className="min-h-[100vh] flex-1 md:min-h-min">
        <div className="flex flex-wrap items-center justify-center gap-3">
          <ToggleGroup
            type="single"
            variant="outline"
            defaultValue="CONFIRME"
            onValueChange={(value) => {
              switch (value) {
                case 'CONFIRME':
                  setStatut('CONFIRME')
                  refetch()
                  break;
                case 'EN_ATTENTE':
                  setStatut('EN_ATTENTE')
                  refetch()
                  break
                case 'ANNULE':
                  setStatut('ANNULE')
                  refetch()
                  break
                case 'all':
                  setStatut(undefined)
                  refetch()
                  break
                default:
                  break;
              }
            }}
          >

            <ToggleGroupItem
              value="CONFIRME"
              aria-label="Toggle bold"
              className=" min-w-20"
            >
              confirmé
            </ToggleGroupItem>
            <ToggleGroupItem
              value="EN_ATTENTE"
              aria-label="Toggle italic"
              className=" min-w-20"
            >
              en attente
            </ToggleGroupItem>
            <ToggleGroupItem
              value="ANNULE"
              aria-label="Toggle italic"
              className=" min-w-20"
            >
              annulé
            </ToggleGroupItem>
            <ToggleGroupItem
              value="all"
              aria-label="Toggle italic"
              className=" min-w-20"
            >
              tout
            </ToggleGroupItem>

          </ToggleGroup>
        </div>
        <NormalDataTable
          columns={RendezVousColumns}
          data={data?.data.content as Array<RendezVousModel>}
          onDelete={() => {
            console.log('deleted')
          }}
          isLoading={isPending}
          pages={data?.data}
          previousPage={() => setPage(page - 1)}
          nextPage={() => setPage(page + 1)}
          apiSearch={(query) => setSearch(query)}
        />
      </div>
    </div>
  )
}