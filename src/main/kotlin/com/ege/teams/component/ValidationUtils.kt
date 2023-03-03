package com.ege.teams.component

import com.ege.teams.service.TeamRoleService
import org.springframework.stereotype.Component

@Component
class ValidationUtils (
    private val teamRoleService: TeamRoleService
) {
    fun validateRole(roleName: String): Boolean {
        val roles = teamRoleService.getRoles()
        return roles.contains(roleName)
    }

    fun validateTeam(teamId: String): Boolean {
        return teamRoleService.doesTeamExistInTeamMemberships(teamId)
    }

    fun validateUserExists(userId: String): Boolean {
        return teamRoleService.doesUserExistInAnyTeam(userId)
    }

    fun validateUserInTeam(teamId: String, userId: String): Boolean {
        val teamMembers = teamRoleService.getTeamMembers(teamId) ?: return false
        return teamMembers.containsKey(userId)
    }

    fun validateDataExists(): Boolean {
        return teamRoleService.checkDataExistsInTeamMemberships()
    }

}