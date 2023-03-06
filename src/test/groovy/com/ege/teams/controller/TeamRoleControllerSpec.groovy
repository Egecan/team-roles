package com.ege.teams.controller

import com.ege.teams.common.Constants
import com.ege.teams.component.DataLoader
import com.ege.teams.model.TeamMembership
import com.ege.teams.service.TeamRoleService
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class TeamRoleControllerSpec extends Specification {

    def teamRoleService = Mockito.mock(TeamRoleService)
    def dataLoader = Mockito.mock(DataLoader)

    @Subject
    TeamRoleController teamRoleController = new TeamRoleController(teamRoleService, dataLoader)

    def "assignRoleToMembership should call the underlying service and return OK if service call is successful"() {
        given:
        def teamId = "team-1"
        def userId = "user-1"
        def roleName = "Developer"

        when:
        ResponseEntity<String> response = teamRoleController.assignRoleToMembership(teamId, userId, roleName)

        then:
        Mockito.verify(teamRoleService, Mockito.times(1)).assignRoleToMembership(teamId, userId, roleName)
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == String.format(Constants.ROLE_ASSIGNED, roleName, userId, teamId)
    }

    def "getRoleForMembership should call the underlying service and return the role name with OK response"() {
        given:
        def teamId = "team-1"
        def userId = "user-1"
        def roleName = "Developer"

        when:
        Mockito.when(teamRoleService.getRoleForMembership(teamId, userId)).thenReturn(roleName)
        ResponseEntity<String> response = teamRoleController.getRoleForMembership(teamId, userId)

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == roleName
    }

    def "getMembershipsForRole should call the underlying service and return the role name with OK response"() {
        given:
        def roleName = "Developer"
        def membershipData = [
                new TeamMembership("team-1", "user-1"),
                new TeamMembership("team-1", "user-2"),
                new TeamMembership("team-2", "user-3"),
        ]

        when:
        Mockito.when(teamRoleService.getMembershipsForRole(roleName)).thenReturn(membershipData)
        ResponseEntity<List<TeamMembership>> response = teamRoleController.getMembershipsForRole(roleName)

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == membershipData
    }

    def "addRole should call the underlying service and return OK if service call is successful"() {
        given:
        def roleName = "Developer"

        when:
        ResponseEntity<String> response = teamRoleController.addRole(roleName)

        then:
        Mockito.verify(teamRoleService, Mockito.times(1)).addRole(roleName)
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == String.format(Constants.ROLE_ADDED, roleName)
    }

    def "getRoles should call the underlying service and return the roles defined with OK response"() {
        given:
        def roles = ["Developer", "Tester", "Product Owner", "BA"] as Set

        when:
        Mockito.when(teamRoleService.getRoles()).thenReturn(roles)
        ResponseEntity<Set<String>> response = teamRoleController.getRoles()

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == roles
    }

    def "assignRoleToUser should call the underlying service and return OK if service call is successful"() {
        given:
        def userId = "user-1"
        def roleName = "Developer"
        def teamsAssigned = 3

        when:
        Mockito.when(teamRoleService.assignRoleToUser(userId, roleName)).thenReturn(teamsAssigned)
        ResponseEntity<String> response = teamRoleController.assignRoleToUser(userId, roleName)

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == String.format(Constants.ROLE_ASSIGNED_USER, roleName, userId, teamsAssigned)
    }

    def "loadData should call the underlying service and return OK if service call is successful"() {
        when:
        ResponseEntity<String> response = teamRoleController.loadData()

        then:
        Mockito.verify(dataLoader, Mockito.times(1)).loadData()
        response.getStatusCode() == HttpStatus.OK
        response.getBody() == Constants.DATA_RELOADED
    }
}
