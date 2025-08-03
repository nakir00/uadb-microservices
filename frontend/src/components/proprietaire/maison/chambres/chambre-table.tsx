import React, { useEffect, useState } from 'react'
import CreateMaisonButton from '../create-maison-button'
import CreateChambreButton from './create-chambre-button'
import type { ChambreModel } from '@/api/queries/chambre'
import type { MaisonModel } from '@/api/queries/maison'
import { api } from '@/api/api'
import { ChambreColumns } from '@/blocs/proprietaire/table/columns/chambre-columns'
import { NormalDataTable } from '@/blocs/proprietaire/table/tables/datatable'
import { subscribe, unsubscribe } from '@/lib/events'

type Props = {
  maisonId: number
}

const ChambresTable = ({ maisonId }: Props) => {
  const [sortBy, setSortBy] = useState<keyof ChambreModel>('id')
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc')
  const [search, setSearch] = useState<string | undefined>(undefined)
  const { isPending, data, refetch } = api.chambre.getAll({
    page,
    size,
    maisonId: maisonId,
    // sort: [[sortBy, sortOrder, 'ignorecase']],
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
    <div className='flex flex-col w-full gap-4'>
      <div className='flex flex-row items-center justify-end gap-4 w-full'>
        <CreateChambreButton maisonId={maisonId} />
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
  )
}

export default ChambresTable
