package com.ege.teams.controller

import com.ege.teams.common.Constants
import com.ege.teams.common.Constants.DATA_RELOADED
import com.ege.teams.common.Constants.ROLE_ADDED
import com.ege.teams.common.Constants.ROLE_ASSIGNED
import com.ege.teams.common.Constants.ROLE_ASSIGNED_USER
import com.ege.teams.component.DataLoader
import com.ege.teams.component.ValidationUtils
import com.ege.teams.model.TeamMembership
import com.ege.teams.service.TeamRoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/team-roles")
class TeamRoleController(
    private val teamRoleService: TeamRoleService,
    private val validationUtils: ValidationUtils,
    private val dataLoader: DataLoader
) {

    @PutMapping("assign-role-to-membership/team/{teamId}/member/{userId}/role/{roleName}")
    fun assignRoleToMembership(
        @PathVariable teamId: String,
        @PathVariable userId: String,
        @PathVariable roleName: String
    ): ResponseEntity<String> {
        if (!validationUtils.validateDataExists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.DATA_NOT_FOUND)
        }
        if (!validationUtils.validateRole(roleName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.INVALID_ROLE)
        }
        if (!validationUtils.validateTeam(teamId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.TEAM_NOT_FOUND)
        }
        if (!validationUtils.validateUserInTeam(teamId, userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND)
        }
        teamRoleService.assignRoleToMembership(teamId, userId, roleName)
        return ResponseEntity.ok(String.format(ROLE_ASSIGNED, roleName, userId, teamId))
    }

    @GetMapping("/get-role-for-membership/team/{teamId}/member/{userId}")
    fun getRoleForMembership(
        @PathVariable teamId: String,
        @PathVariable userId: String
    ): ResponseEntity<String> {
        if (!validationUtils.validateDataExists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.DATA_NOT_FOUND)
        }
        if (!validationUtils.validateTeam(teamId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.TEAM_NOT_FOUND)
        }
        if (!validationUtils.validateUserInTeam(teamId, userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND)
        }
        val role = teamRoleService.getRoleForMembership(teamId, userId)
        return ResponseEntity.ok(role)
    }

    @GetMapping("/get-memberships-for-role/{roleName}")
    fun getMembershipsForRole(@PathVariable roleName: String): ResponseEntity<List<TeamMembership>> {
        if (!validationUtils.validateDataExists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
        if (!validationUtils.validateRole(roleName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emptyList())
        }
        val members = teamRoleService.getMembershipsForRole(roleName)
        return ResponseEntity.ok(members)
    }

    @PostMapping("/add-role/{roleName}")
    fun addRole(@PathVariable roleName: String): ResponseEntity<String> {
        teamRoleService.addRole(roleName)
        return ResponseEntity.ok(String.format(ROLE_ADDED, roleName))
    }

    @GetMapping("/get-roles")
    fun getRoles(): ResponseEntity<Set<String>> {
        val roles = teamRoleService.getRoles()
        return ResponseEntity.ok(roles)
    }

    @PutMapping("/assign-role-to-user/user/{userId}/role/{roleName}")
    fun assignRoleToUser(
        @PathVariable userId: String,
        @PathVariable roleName: String
    ): ResponseEntity<String> {
        if (!validationUtils.validateDataExists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.DATA_NOT_FOUND)
        }
        if (!validationUtils.validateRole(roleName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.INVALID_ROLE)
        }
        if (!validationUtils.validateUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND_ANYWHERE)
        }
        val teams = teamRoleService.assignRoleToUser(userId, roleName)
        return ResponseEntity.ok(String.format(ROLE_ASSIGNED_USER, roleName, userId, teams))
    }

    @PostMapping("/load-data")
    fun loadData(): ResponseEntity<String> {
        dataLoader.loadData()
        return ResponseEntity.ok(DATA_RELOADED)
    }

}
