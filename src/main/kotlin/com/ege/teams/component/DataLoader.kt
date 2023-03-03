package com.ege.teams.component

import com.ege.teams.service.TeamRoleService
import com.ege.teams.service.TeamService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val teamService: TeamService,
    private val teamRoleService: TeamRoleService
) {

    @EventListener(ApplicationReadyEvent::class)
    fun loadData() {
        try {
            val teamList = teamService.getAllTeams()
            teamList?.forEach { teamResponse ->
                val team = teamService.getTeam(teamResponse.id)
                team?.let {
                    teamRoleService.fillInitialTeamRoles(it)
                }
            }
        } catch (ex: Exception) {
            println("Failed to load data: ${ex.message}")
        }
    }
}