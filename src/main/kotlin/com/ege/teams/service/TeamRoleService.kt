package com.ege.teams.service

import com.ege.teams.common.Constants
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
        validateDataExists()
        validateRoleExists(roleName)
        validateTeamExists(teamId)
        validateUserInTeam(teamId, userId)
        teamMemberRoleData[teamId]?.set(userId, roleName)
    }

    fun getRoleForMembership(teamId: String, userId: String): String? {
        validateDataExists()
        validateTeamExists(teamId)
        validateUserInTeam(teamId, userId)
        return teamMemberRoleData[teamId]?.get(userId)
    }

    fun getMembershipsForRole(roleName: String): List<TeamMembership> {
        validateDataExists()
        validateRoleExists(roleName)
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

    fun getTeamMembers(teamId: String): MutableMap<String, String>? {
        return teamMemberRoleData[teamId]
    }

    fun assignRoleToUser(userId: String, roleName: String): Int {
        validateDataExists()
        validateRoleExists(roleName)
        validateUserExists(userId)
        val memberships = teamMemberRoleData.filterValues { it.containsKey(userId) }
        memberships.forEach { (teamId, teamMembers) ->
            teamMembers[userId] = roleName
            teamMemberRoleData[teamId] = teamMembers
        }
        return memberships.size
    }

    fun getTeamMemberRoleData(): MutableMap<String, MutableMap<String, String>> {
        return teamMemberRoleData
    }

    fun setTeamMemberRoleData(data: MutableMap<String, MutableMap<String, String>>) {
        teamMemberRoleData.clear()
        teamMemberRoleData.putAll(data)
    }

    fun validateDataExists() {
        if (teamMemberRoleData.isEmpty())
            throw IllegalStateException(Constants.DATA_NOT_FOUND)
    }

    fun validateRoleExists(roleName: String) {
        if (!roles.contains(roleName))
            throw NoSuchElementException(Constants.INVALID_ROLE)
    }

    fun validateTeamExists(teamId: String) {
        if (!teamMemberRoleData.containsKey(teamId))
            throw NoSuchElementException(Constants.TEAM_NOT_FOUND)
    }

    fun validateUserExists(userId: String) {
        val memberships = teamMemberRoleData.filterValues { it.containsKey(userId) }
        if (memberships.isEmpty())
            throw NoSuchElementException(Constants.USER_NOT_FOUND_ANYWHERE)
    }

    fun validateUserInTeam(teamId: String, userId: String) {
        val teamMembers = teamMemberRoleData[teamId]
        if (teamMembers.isNullOrEmpty() || !teamMembers.containsKey(userId))
            throw NoSuchElementException(Constants.USER_NOT_FOUND)
    }

}
