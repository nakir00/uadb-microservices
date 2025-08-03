import { createFileRoute, redirect, useSearch } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import { z } from 'zod'
import type { ContratModel } from '@/api/queries/contrat'
import { api, instance } from '@/api/api'
import { NormalDataTable } from '@/blocs/proprietaire/table/tables/datatable'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'
import { subscribe, unsubscribe } from '@/lib/events'
import { useUser } from '@/hooks/user'
import { ContratColumns } from '@/blocs/proprietaire/table/columns/contrat-columns'
import CreateContratButton from '@/components/proprietaire/rendez-vous/create-contrat-button'

const productSearchSchema = z.object({
  locataireId: z.number().positive().optional(),
  chambreId: z.number().positive().optional()
})

export const Route = createFileRoute('/proprietaire/_proprietaireLayout/contrats/')({
  validateSearch: productSearchSchema,
  component: RouteComponent,
  loader({ context }) {
      if (!context.auth.user) {
        throw redirect({ to: '/auth/login', from: '/proprietaire/contrats' })
      }
      return { proprietaireId: context.auth.user.id }
    },
})

function RouteComponent() {
  // const { proprietaireId } = Route.useLoaderData()
  const { user } = useUser()
  const { locataireId, chambreId } = Route.useSearch()
  const [sortBy, setSortBy] = useState<keyof ContratModel>('id')
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  // const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc')
  const [statut, setStatut] = useState<"ACTIF" | "RESILIE" | undefined>('ACTIF')
  const [search, setSearch] = useState<string | undefined>('')
  
  const { isPending, data, refetch } = api.contrat.getAll({
    page,
    size,
    statut: statut,
    
    // sort: [[sortBy, sortOrder, 'ignorecase']],
  })

  useEffect(() => {
    subscribe('refresh_proprietaire_contrat_table', (e) => {
      refetch()
    })
    instance.get('')

    return () => {
      unsubscribe('refresh_proprietaire_contrat_table', () => {})
    }
  }, [])

  return (
    <div className="flex flex-col gap-4 lg:gap-6 py-4 lg:py-6 ">
      {/* Page intro */}
      <div className="flex items-center justify-between gap-4">
        <div className="space-y-1">
          <h1 className="text-2xl font-semibold">Contrats</h1>
          <p className="text-sm text-muted-foreground">
            Gestion des contrats de location de vos propriétés
          </p>
        </div>
        
        {/* {user && user.id && locataireId && chambreId && (
          <CreateContratButton 
            proprietaireId={user.id} 
            locataireId={locataireId} 
            chambreId={chambreId} 
          />
        )} */}
      </div>

      <div className="min-h-[100vh] flex-1 md:min-h-min">
        <div className="flex flex-wrap items-center justify-center gap-3">
          <ToggleGroup
            type="single"
            variant="outline"
            defaultValue="ACTIF"
            onValueChange={(value) => {
              switch (value) {
                case 'ACTIF':
                  setStatut('ACTIF')
                  refetch()
                  break;
                case 'RESILIE':
                  setStatut('RESILIE')
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
              value="ACTIF"
              aria-label="Contrats actifs"
              className=" min-w-20"
            >
              Actifs
            </ToggleGroupItem>
            <ToggleGroupItem
              value="RESILIE"
              aria-label="Contrats résiliés"
              className=" min-w-20"
            >
              Résiliés
            </ToggleGroupItem>
            <ToggleGroupItem
              value="all"
              aria-label="Tous les contrats"
              className=" min-w-20"
            >
              Tous
            </ToggleGroupItem>

          </ToggleGroup>
        </div>
        <NormalDataTable
          columns={ContratColumns}
          data={data?.data.content as Array<ContratModel>}
          onDelete={() => {
            console.log('contrat deleted')
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