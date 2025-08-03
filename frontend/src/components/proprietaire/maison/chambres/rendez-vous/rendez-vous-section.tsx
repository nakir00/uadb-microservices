import { useEffect, useState } from "react"
import type { RendezVousModel } from "@/api/queries/rendez-vous"
import { api } from "@/api/api"
import { RendezVousColumns } from "@/blocs/locataire/table/columns/rendez-vous-columns"
import { NormalDataTable } from "@/blocs/proprietaire/table/tables/datatable"
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group"
import { useUser } from "@/hooks/user"
import { subscribe, unsubscribe } from "@/lib/events"

type Props ={
    chambreId: number
}

export const RendezVousSection = ({ chambreId }:Props) => {
  // const { proprietaireId } = Route.useLoaderData()
  const { user } = useUser()
  const [sortBy, setSortBy] = useState <keyof RendezVousModel> ('id')
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
    chambreId,
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