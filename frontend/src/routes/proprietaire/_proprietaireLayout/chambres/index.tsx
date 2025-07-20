import { createFileRoute, redirect } from '@tanstack/react-router'
import { useEffect, useState } from 'react'
import type { MaisonModel } from '@/api/queries/maison'
import type { ChambreModel } from '@/api/queries/chambre'
import { StatsGrid } from '@/components/stats-grid'
import { api } from '@/api/api'
import { NormalDataTable } from '@/blocs/proprietaire/table/tables/datatable'
import { subscribe, unsubscribe } from '@/lib/events'
import CreateMaisonButton from '@/components/proprietaire/maison/create-maison-button'
import { ChambreColumns } from '@/blocs/proprietaire/table/columns/chambre-columns'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'

export const Route = createFileRoute(
  '/proprietaire/_proprietaireLayout/chambres/',
)({
  loader({ context }) {
    if (!context.auth.user) {
      throw redirect({ to: '/auth/login', from: '/proprietaire/chambres' })
    }
    return { proprietaireId: context.auth.user.id! }
  },
  component: RouteComponent,
})

function RouteComponent() {
  const { proprietaireId } = Route.useLoaderData()
  const maisonCreate = api.maison.create()
  const [sortBy, setSortBy] = useState<keyof ChambreModel>('disponible')
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [disponible, setDisponible] = useState<boolean| undefined>(true)
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc')
  const [search, setSearch] = useState<string | undefined>('')
  console.log(disponible);
  
  const { isPending, data, refetch } = api.chambre.getAll({
    page,
    size,
    // sort: [[sortBy, sortOrder, 'ignorecase']],
    disponible,
    proprietaireId,
    titre: search,
  })

  useEffect(() => {
    subscribe('refresh_chambre_table', (e) => {
      refetch()
    })

    return () => {
      unsubscribe('refresh_chambre_table', () => {})
    }
  }, [])

  return (
    <div className="flex flex-col gap-4 lg:gap-6 py-4 lg:py-6 ">
      {/* Page intro */}
      <div className="flex items-center justify-between gap-4">
        <div className="space-y-1">
          <h1 className="text-2xl font-semibold">Chambres</h1>
          <p className="text-sm text-muted-foreground">
            liste des chambres que vous gérer
          </p>
        </div>
        <CreateMaisonButton />
      </div>
      {/* Numbers */}
      <StatsGrid
        stats={[
          {
            title: 'Connections',
            value: '427,296',
            change: {
              value: '+12%',
              trend: 'up',
            },
            icon: (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width={20}
                height={20}
                fill="currentColor"
              >
                <path d="M9 0v2.013a8.001 8.001 0 1 0 5.905 14.258l1.424 1.422A9.958 9.958 0 0 1 10 19.951c-5.523 0-10-4.478-10-10C0 4.765 3.947.5 9 0Zm10.95 10.95a9.954 9.954 0 0 1-2.207 5.329l-1.423-1.423a7.96 7.96 0 0 0 1.618-3.905h2.013ZM11.002 0c4.724.47 8.48 4.227 8.95 8.95h-2.013a8.004 8.004 0 0 0-6.937-6.937V0Z" />
              </svg>
            ),
          },
          {
            title: 'Contacts',
            value: '37,429',
            change: {
              value: '+42%',
              trend: 'up',
            },
            icon: (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width={18}
                height={19}
                fill="currentColor"
              >
                <path d="M2 9.5c0 .313.461.858 1.53 1.393C4.914 11.585 6.877 12 9 12c2.123 0 4.086-.415 5.47-1.107C15.538 10.358 16 9.813 16 9.5V7.329C14.35 8.349 11.827 9 9 9s-5.35-.652-7-1.671V9.5Zm14 2.829C14.35 13.349 11.827 14 9 14s-5.35-.652-7-1.671V14.5c0 .313.461.858 1.53 1.393C4.914 16.585 6.877 17 9 17c2.123 0 4.086-.415 5.47-1.107 1.069-.535 1.53-1.08 1.53-1.393v-2.171ZM0 14.5v-10C0 2.015 4.03 0 9 0s9 2.015 9 4.5v10c0 2.485-4.03 4.5-9 4.5s-9-2.015-9-4.5ZM9 7c2.123 0 4.086-.415 5.47-1.107C15.538 5.358 16 4.813 16 4.5c0-.313-.461-.858-1.53-1.393C13.085 2.415 11.123 2 9 2c-2.123 0-4.086.415-5.47 1.107C2.461 3.642 2 4.187 2 4.5c0 .313.461.858 1.53 1.393C4.914 6.585 6.877 7 9 7Z" />
              </svg>
            ),
          },
          {
            title: 'Value',
            value: '$82,439',
            change: {
              value: '+37%',
              trend: 'up',
            },
            icon: (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width={20}
                height={20}
                fill="currentColor"
              >
                <path d="M10 0c5.523 0 10 4.477 10 10s-4.477 10-10 10S0 15.523 0 10 4.477 0 10 0Zm0 2a8 8 0 1 0 0 16 8 8 0 0 0 0-16Zm3.833 3.337a.596.596 0 0 1 .763.067.59.59 0 0 1 .063.76c-2.18 3.046-3.38 4.678-3.598 4.897a1.5 1.5 0 0 1-2.122-2.122c.374-.373 2.005-1.574 4.894-3.602ZM15.5 9a1 1 0 1 1 0 2 1 1 0 0 1 0-2Zm-11 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2Zm2.318-3.596a1 1 0 1 1-1.414 1.414 1 1 0 0 1 1.414-1.414ZM10 3.5a1 1 0 1 1 0 2 1 1 0 0 1 0-2Z" />
              </svg>
            ),
          },
          {
            title: 'Referrals',
            value: '3,497',
            change: {
              value: '-17%',
              trend: 'down',
            },
            icon: (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width={21}
                height={21}
                fill="currentColor"
              >
                <path d="m14.142.147 6.347 6.346a.5.5 0 0 1-.277.848l-1.474.23-5.656-5.657.212-1.485a.5.5 0 0 1 .848-.282ZM2.141 19.257c3.722-3.33 7.995-4.327 12.643-5.52l.446-4.017-4.297-4.298-4.018.447c-1.192 4.648-2.189 8.92-5.52 12.643L0 17.117c2.828-3.3 3.89-6.953 5.303-13.081l6.364-.708 5.657 5.657-.707 6.364c-6.128 1.415-9.782 2.475-13.081 5.304L2.14 19.258Zm5.284-6.029a2 2 0 1 1 2.828-2.828 2 2 0 0 1-2.828 2.828Z" />
              </svg>
            ),
          },
        ]}
      />
      {/* Table */}

      <div className="min-h-[100vh] flex-1 md:min-h-min">
        <div className="flex flex-wrap items-center justify-center gap-3">
          <ToggleGroup
            type="single"
            defaultValue="location"
            onValueChange={(value) => {
              switch (value) {
                case 'location':
                  setDisponible(true)
                  break;
                case 'libre':
                  setDisponible(false)
                  break
                case 'all':
                  setDisponible(undefined)
                  break
              
                default:
                  break;
              }
            }}
          >
            <ToggleGroupItem
              value="location"
              aria-label="Toggle bold"
              className=" min-w-20"
            >
              En Location
            </ToggleGroupItem>
            <ToggleGroupItem
              value="libre"
              aria-label="Toggle italic"
              className=" min-w-20"
            >
              Libre
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
          searchColumnName={'titre'}
          columns={ChambreColumns}
          data={data?.data.content as Array<ChambreModel>} // {maisonRead.data?.content as Array<MaisonModel>}
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
