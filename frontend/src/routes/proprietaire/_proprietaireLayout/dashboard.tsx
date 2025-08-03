import { createFileRoute } from '@tanstack/react-router'
import React, { useMemo, useState } from 'react'
import { AlertTriangle, Calendar, CalendarDays, CheckCircle, Clock, DollarSign, Download, Eye, Filter, Search, TrendingDown, TrendingUp, Users } from 'lucide-react'
import type { PaiementModel } from '@/api/queries/paiement'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
// Table components inline - shadcn/ui table not available in this environment
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { instance } from '@/api/api'

export const Route = createFileRoute('/proprietaire/_proprietaireLayout/dashboard')({
  async loader() {
      return await instance.get<Array<PaiementModel>>(
        `paiement/all?`,
      )
    },
  component: RouteComponent,
})

export default function RouteComponent() {
  const { data } = Route.useLoaderData()
  const [paiements] = useState<Array<PaiementModel>>(data)
  const [activeTab, setActiveTab] = useState('all')
  const [searchTerm, setSearchTerm] = useState('')

  // Calculs des statistiques
  const statistics = useMemo(() => {
    const today = new Date()
    const totalPaiements = paiements.length
    console.log(paiements)
    const paiementsPaye = paiements.filter(p => p.statut === 'PAYE')
    const paiementsImpaye = paiements.filter(p => p.statut === 'IMPAYE')
    const paiementsEnRetard = paiements.filter(p => 
      p.statut === 'IMPAYE' && new Date(p.dateEcheance) < today
    )

    const montantTotal = paiements.reduce((sum, p) => sum + p.montant, 0)
    const montantPaye = paiementsPaye.reduce((sum, p) => sum + p.montant, 0)
    const montantImpaye = paiementsImpaye.reduce((sum, p) => sum + p.montant, 0)
    const montantEnRetard = paiementsEnRetard.reduce((sum, p) => sum + p.montant, 0)

    return {
      totalPaiements,
      paiementsPaye: paiementsPaye.length,
      paiementsImpaye: paiementsImpaye.length,
      paiementsEnRetard: paiementsEnRetard.length,
      montantTotal,
      montantPaye,
      montantImpaye,
      montantEnRetard,
      tauxPaiement: totalPaiements > 0 ? (paiementsPaye.length / totalPaiements) * 100 : 0
    }
  }, [paiements])

  // Filtrage des paiements
  const filteredPaiements = useMemo(() => {
    return paiements.filter(paiement => {
      // Filtrage par onglet actif
      if (activeTab === 'paye' && paiement.statut !== 'PAYE') return false
      if (activeTab === 'impaye' && paiement.statut !== 'IMPAYE') return false
      if (activeTab === 'retard') {
        const isEnRetard = paiement.statut === 'IMPAYE' && new Date(paiement.dateEcheance) < new Date()
        if (!isEnRetard) return false
      }

      // Recherche textuelle
      if (searchTerm) {
        const term = searchTerm.toLowerCase()
        if (!paiement.contrat.id!.toString().includes(term) && 
            !paiement.montant.toString().includes(term)) {
          return false
        }
      }

      // Critères de recherche avancée
      
      return true
    })
  }, [paiements, activeTab, searchTerm])

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      minimumFractionDigits: 0
    }).format(amount)
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('fr-FR')
  }

  const getStatutBadge = (statut: PaiementModel['statut'], dateEcheance: string) => {
    const isEnRetard = statut === 'IMPAYE' && new Date(dateEcheance) < new Date()
    
    switch (statut) {
      case 'PAYE':
        return <Badge className="bg-green-100 text-green-800"><CheckCircle className="w-3 h-3 mr-1" />Payé</Badge>
      case 'IMPAYE':
        return isEnRetard 
          ? <Badge className="bg-red-100 text-red-800"><AlertTriangle className="w-3 h-3 mr-1" />En retard</Badge>
          : <Badge className="bg-orange-100 text-orange-800"><Clock className="w-3 h-3 mr-1" />Impayé</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-800">Inconnu</Badge>
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Dashboard Paiements</h1>
            <p className="text-gray-600 mt-1">Gérez et suivez tous vos paiements</p>
          </div>
          <div className="flex gap-3">
            <Button variant="outline" size="sm">
              <Download className="w-4 h-4 mr-2" />
              Exporter
            </Button>
            <Button size="sm">
              <Filter className="w-4 h-4 mr-2" />
              Filtres avancés
            </Button>
          </div>
        </div>

        {/* Statistiques */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total Paiements</CardTitle>
              <DollarSign className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{statistics.totalPaiements}</div>
              <p className="text-xs text-muted-foreground">
                {formatCurrency(statistics.montantTotal)}
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Paiements Payés</CardTitle>
              <CheckCircle className="h-4 w-4 text-green-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">{statistics.paiementsPaye}</div>
              <p className="text-xs text-muted-foreground">
                {formatCurrency(statistics.montantPaye)}
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Paiements Impayés</CardTitle>
              <AlertTriangle className="h-4 w-4 text-orange-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-orange-600">{statistics.paiementsImpaye}</div>
              <p className="text-xs text-muted-foreground">
                {formatCurrency(statistics.montantImpaye)}
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">En Retard</CardTitle>
              <TrendingDown className="h-4 w-4 text-red-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-red-600">{statistics.paiementsEnRetard}</div>
              <p className="text-xs text-muted-foreground">
                {formatCurrency(statistics.montantEnRetard)}
              </p>
            </CardContent>
          </Card>
        </div>

        {/* Taux de paiement */}
        <Card>
          <CardHeader>
            <CardTitle>Taux de Paiement</CardTitle>
            <CardDescription>Pourcentage des paiements effectués</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex items-center space-x-4">
              <div className="flex-1 bg-gray-200 rounded-full h-2">
                <div 
                  className="bg-green-600 h-2 rounded-full transition-all duration-300"
                  style={{ width: `${statistics.tauxPaiement}%` }}
                />
              </div>
              <span className="text-sm font-medium">{statistics.tauxPaiement.toFixed(1)}%</span>
            </div>
          </CardContent>
        </Card>

        {/* Liste des paiements */}
        <Card>
          <CardHeader>
            <CardTitle>Liste des Paiements</CardTitle>
            <CardDescription>
              {filteredPaiements.length} paiement(s) trouvé(s)
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Tabs value={activeTab} onValueChange={setActiveTab}>
              <TabsList className="grid w-full grid-cols-4">
                <TabsTrigger value="all">Tous ({statistics.totalPaiements})</TabsTrigger>
                <TabsTrigger value="paye">Payés ({statistics.paiementsPaye})</TabsTrigger>
                <TabsTrigger value="impaye">Impayés ({statistics.paiementsImpaye})</TabsTrigger>
                <TabsTrigger value="retard">En retard ({statistics.paiementsEnRetard})</TabsTrigger>
              </TabsList>

              <TabsContent value={activeTab} className="mt-6">
                <div className="rounded-md border overflow-hidden">
                  <div className="overflow-x-auto">
                    <table className="w-full">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contrat</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Montant</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Échéance</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date de paiement</th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {filteredPaiements.map((paiement) => (
                          <tr key={paiement.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">#{paiement.id}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Contrat #{paiement.contrat.id!}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">
                              {formatCurrency(paiement.montant)}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              {getStatutBadge(paiement.statut, paiement.dateEcheance)}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              <div className="flex items-center gap-1">
                                <CalendarDays className="w-4 h-4 text-gray-400" />
                                {formatDate(paiement.dateEcheance)}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {paiement.datePaiement ? (
                                <div className="flex items-center gap-1 text-green-600">
                                  <CheckCircle className="w-4 h-4" />
                                  {formatDate(paiement.datePaiement)}
                                </div>
                              ) : (
                                <span className="text-gray-400">-</span>
                              )}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              <Button variant="outline" size="sm">
                                <Eye className="w-4 h-4 mr-1" />
                                Voir
                              </Button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>

                {filteredPaiements.length === 0 && (
                  <div className="text-center py-8 text-gray-500">
                    <AlertTriangle className="w-12 h-12 mx-auto mb-3 text-gray-300" />
                    <p>Aucun paiement trouvé avec ces critères</p>
                  </div>
                )}
              </TabsContent>
            </Tabs>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
