import { Eye, MoreHorizontal, SquarePen, Trash } from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { Link, useNavigate } from '@tanstack/react-router'
import {
  
  InfoMaisonForm
  
} from '../../forms/maison/info-maison-form'
import type { MaisonModel } from '@/api/queries/maison'
import type { ColumnDef } from '@tanstack/react-table'
import type {InfoMaisonFormType} from '../../forms/maison/info-maison-form';
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { api } from '@/api/api'
import { publish } from '@/lib/events'

export const MaisonColumns: Array<ColumnDef<MaisonModel>> = [
  {
    accessorKey: 'id',
    header: 'id',
  },
  {
    accessorKey: 'description',
    header: 'description',
  },
  {
    accessorKey: 'adresse',
    header: 'adresse',
  },
  {
    accessorKey: 'longitude',
    header: 'longitude',
  },
  {
    accessorKey: 'latitude',
    header: 'latitude',
  },
  {
    accessorKey: 'creeLe',
    header: 'creeLe',
  },
  {
    id: 'chambres',
    cell: ({ row }) => {
      const maison = row.original
      if (!maison.id) {
        return <Button disabled> chambres</Button>
      }

      return <Link to={`/proprietaire/maisons/$maisonId`} params={{maisonId: maison.id.toString()}}> <Button> <Eye /> </Button></Link>
    }
  },
  {
    id: 'actions',
    cell: ({ row }) => {
      const maison = row.original
      const [open, setOpen] = useState(false)
      const maisonUpdate = api.maison.update()
      const maisonDelete = api.maison.delete() 
      const [triggered, setTriggered] = useState('')

      function closeDialog() {
        setOpen(false)
      }

       const deleteId = () => {
        if(!maison.id){
          toast.error('Erreur !!!',{
                description:
                  "ligne non définie",
              })
          return
        }

        maisonDelete.mutate(maison.id, {
          onSettled(data, error, variables, context) {
            closeDialog()
            publish('refresh_maison_table',{})
          },
          onSuccess(data, variables, context) {
            toast.success('Suppression effectuée',{
              description: "La suppression de la maison est un succés",
            })
          },
          onError(error, variables, context) {
            toast.error('Erreur !!!',{
              description: " Une erreur s'est produite lors de la supression",
            })
          },
        })
      }

      const updateId = ({
        description,
        adresse,
        lat,
        lon,
      }: InfoMaisonFormType) => {
        if(!maison.id){
          toast.error('Erreur !!!',{
                description:
                  "ligne non définie",
              })
          return
        }

        const formData = new FormData()
        formData.append('description', description)
        formData.append('adresse', adresse)
        formData.append('latitude', lat)
        formData.append('longitude', lon)
        
        maisonUpdate.mutate({data: formData, id: maison.id} ,
          {
            onSettled(data, error, variables, context) { 
              closeDialog()
              publish('refresh_maison_table',{})
            },
            onSuccess(data, variables, context) {
              toast.success('Modification effectuée',{
                description: "La modification de l'unité est un succés",
              })
            },
            onError(error, variables, context) {
              toast.error('Erreur !!!',{
                description:
                  " Une erreur s'est produite lors de la modification",
              })
            },
          },
        )
      }

      return (
        <Dialog open={open} onOpenChange={setOpen}>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="w-8 h-8 p-0">
                <span className="sr-only">Open menu</span>
                <MoreHorizontal className="w-4 h-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Actions</DropdownMenuLabel>

              <DialogTrigger asChild>
                <DropdownMenuItem
                  className="flex flex-row px-2 justify-between"
                  onClick={() => {
                    setTriggered('modifier')
                  }}
                >
                  <span>modifier</span>
                  <SquarePen />
                </DropdownMenuItem>
              </DialogTrigger>

              <DialogTrigger asChild>
                <DropdownMenuItem
                  className="flex flex-row px-2 justify-between text-red-500"
                  onClick={() => {
                    setTriggered('delete')
                  }}
                >
                  Supprimer
                  <Trash />
                </DropdownMenuItem>
              </DialogTrigger>
            </DropdownMenuContent>
          </DropdownMenu>
          <DialogContent>
            {triggered === 'modifier' && (
              <>
                <DialogHeader>
                  <DialogTitle>Modification</DialogTitle>
                  <DialogDescription>
                    modification de {maison.description}
                  </DialogDescription>
                </DialogHeader>
                <div className="p-8 md:p-0">
                  <InfoMaisonForm
                    lat={maison.latitude}
                    lon={maison.longitude}
                    onSoumis={(values) => updateId(values)}
                    {...maison}
                  />
                </div>
              </>
            )}

            {triggered === 'delete' && (
              <>
                <DialogHeader>
                  <DialogTitle>Supression Maison</DialogTitle>
                  <DialogDescription>
                    etes vous sur de vouloir suprimer cette maison ?
                  </DialogDescription>
                </DialogHeader>
                <DialogFooter>
                  <div className="flex flex-row w-full gap-4 mb-8">
                    <Button
                      className="w-1/3"
                      variant={'destructive'}
                      onClick={() => deleteId()}
                    >
                      OUI
                    </Button>
                    <Button
                      className="w-2/3"
                      onClick={() => {
                        closeDialog()
                      }}
                    >
                      NON
                    </Button>
                  </div>
                </DialogFooter>
              </>
            )}

          </DialogContent>
        </Dialog>
      )
    },
  },
]
