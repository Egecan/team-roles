package com.ege.teams.service

import com.ege.teams.common.Constants.DEFAULT_ROLE
import com.ege.teams.common.Constants.PO_ROLE
import com.ege.teams.common.Constants.TESTER_ROLE
import com.ege.teams.model.TeamDetailsResponse
import com.ege.teams.model.TeamMembership
import org.springframework.stereotype.Service

@Service
class TeamRoleService {

    private val teamMemberRoleData: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
    private val roles: MutableSet<String> = mutableSetOf(DEFAULT_ROLE, PO_ROLE, TESTER_ROLE)

    fun fillInitialTeamRoles(team: TeamDetailsResponse) {
        team.teamMemberIds.forEach { userId ->
            teamMemberRoleData.getOrPut(team.id, ::mutableMapOf)[userId] = DEFAULT_ROLE
        }
        // If team lead should have a role assigned to it as well, we could use the line below. I assumed not.
        // team.teamLeadId?.let { teamMemberships.getOrPut(team.id, ::mutableMapOf)[it] = defaultRole }
    }

    fun assignRoleToMembership(teamId: String, userId: String, roleName: String) {
        teamMemberRoleData[teamId]?.set(userId, roleName)
    }

    fun getRoleForMembership(teamId: String, userId: String): String? {
        return teamMemberRoleData[teamId]?.get(userId)
    }

    fun getMembershipsForRole(roleName: String): List<TeamMembership> {
        val memberships = mutableListOf<TeamMembership>()
        for ((teamId, teamMembers) in teamMemberRoleData) {
            for ((userId, role) in teamMembers) {
                if (role == roleName) {
                    memberships.add(TeamMembership(teamId, userId))
                }
            }
        }
        return memberships
    }

    fun addRole(roleName: String) {
        roles.add(roleName)
    }

    fun getRoles(): Set<String> {
        return roles.toSet()
    }

    fun doesTeamExistInTeamMemberships(teamId: String): Boolean {
        return teamMemberRoleData.containsKey(teamId)
    }

    fun getTeamMembers(teamId: String): MutableMap<String, String>? {
        return teamMemberRoleData[teamId]
    }

    fun assignRoleToUser(userId: String, roleName: String): Int {
        val memberships = teamMemberRoleData.filterValues { it.containsKey(userId) }
        memberships.forEach { (teamId, teamMembers) ->
            teamMembers[userId] = roleName
            teamMemberRoleData[teamId] = teamMembers
        }
        return memberships.size
    }

    fun doesUserExistInAnyTeam(userId: String): Boolean {
        val memberships = teamMemberRoleData.filterValues { it.containsKey(userId) }
        return memberships.isNotEmpty()
    }

    fun checkDataExistsInTeamMemberships(): Boolean {
        return teamMemberRoleData.isNotEmpty()
    }

    fun getTeamMemberRoleData(): MutableMap<String, MutableMap<String, String>> {
        return teamMemberRoleData
    }

    fun setTeamMemberRoleData(data: MutableMap<String, MutableMap<String, String>>) {
        teamMemberRoleData.clear()
        teamMemberRoleData.putAll(data)
    }
}
