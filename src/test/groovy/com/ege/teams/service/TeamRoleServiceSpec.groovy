package com.ege.teams.service

import com.ege.teams.common.Constants
import com.ege.teams.model.TeamDetailsResponse
import spock.lang.Specification
import spock.lang.Subject

class TeamRoleServiceSpec extends Specification {

    @Subject
    TeamRoleService teamRoleService = new TeamRoleService()

    def DEF_ROLE = "Def Role"
    def role1 = "role-1"
    def role2 = "role-2"

    def setup() {
        teamRoleService.addRole(role1)
        teamRoleService.addRole(role2)
    }

    def "fillInitialTeamRoles should fill default role for all team members"() {
        given:
        def team = new TeamDetailsResponse("team-1", "random-team","some-id", ["user-1", "user-2", "user-3"])

        when:
        teamRoleService.fillInitialTeamRoles(team)

        then:
        teamRoleService.getTeamMembers(team.id).size() == team.teamMemberIds.size()
        teamRoleService.getTeamMembers(team.id).every { it.value == Constants.DEFAULT_ROLE }
    }

    def "assignRoleToMembership should update the role for the given user in the given team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role1]]

        when:
        teamRoleService.assignRoleToMembership("team-1", "user-1", role2)

        then:
        teamRoleService.teamMemberRoleData["team-1"]["user-1"] == role2
    }

    def "assignRoleToMembership should fail validation when team is not found"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role1]]

        when:
        teamRoleService.assignRoleToMembership("team-2", "user-1", role2)

        then:
        thrown(NoSuchElementException)
    }

    def "getRoleForMembership should return the correct role for a given user in a given team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role1]]

        when:
        def result = teamRoleService.getRoleForMembership("team-1", "user-1")

        then:
        result == role1
    }

    def "getRoleForMembership should fail validation when the given user is not a member of the given team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role1]]

        when:
        teamRoleService.getRoleForMembership("team-1", "user-2")

        then:
        thrown(NoSuchElementException)
    }

    def "getRoleForMembership should fail validation when the given team does not exist in the data"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role1]]

        when:
        teamRoleService.getRoleForMembership("team-2", "user-1")

        then:
        thrown(NoSuchElementException)
    }

    def "getMembershipsForRole should throw IllegalStateException when no data is loaded"() {
        given:
        teamRoleService.teamMemberRoleData = [:]

        when:
        teamRoleService.getMembershipsForRole(role1)

        then:
        thrown(IllegalStateException)
    }

    def "getMembershipsForRole should return an empty list when no members have the specified role"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE, "user-2": DEF_ROLE]]

        when:
        def result = teamRoleService.getMembershipsForRole(role2)

        then:
        result.isEmpty()
    }

    def "getMembershipsForRole should return memberships for a single team when a single team has members with the role"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role2, "user-2": DEF_ROLE]]

        when:
        def result = teamRoleService.getMembershipsForRole(role2)

        then:
        result.size() == 1
        result.get(0).teamId == "team-1"
        result.get(0).userId == "user-1"
    }

    def "getMembershipsForRole should return memberships for multiple teams when multiple teams have members with the specified role"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": role1, "user-2": DEF_ROLE],
                                              "team-2": ["user-3": role1, "user-4": DEF_ROLE]]

        when:
        def result = teamRoleService.getMembershipsForRole(role1)

        then:
        result.size() == 2
        result.find { it.teamId == "team-1" } != null
        result.find { it.teamId == "team-2" } != null
    }

    def "addRole should add a new role to the roles set"() {
        given:
        def roleName = "very-new-role"
        def initialRoles = teamRoleService.roles

        when:
        teamRoleService.addRole(roleName)

        then:
        teamRoleService.roles.contains(roleName)
        teamRoleService.roles.size() == initialRoles.size() + 1
    }

    def "getRoles should return roles set"() {
        when:
        def result = teamRoleService.getRoles()

        then:
        result.contains(Constants.DEFAULT_ROLE)
        result.contains(Constants.PO_ROLE)
        result.contains(Constants.TESTER_ROLE)
    }

    def "getTeamMembers should return the correct team members for the given team"() {
        given:
        def members = ["user-1": role1, "user-2": role2]
        teamRoleService.teamMemberRoleData = ["team-1": members]

        when:
        def result = teamRoleService.getTeamMembers("team-1")

        then:
        result == members
    }

    def "getTeamMembers should return null if the team does not exist"() {
        given:
        teamRoleService.teamMemberRoleData = [:]

        when:
        def result = teamRoleService.getTeamMembers("team-1")

        then:
        result == null
    }


    def "assignRoleToUser should assign role to the given user in all teams"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE], "team-2": ["user-1": DEF_ROLE]]

        when:
        def result = teamRoleService.assignRoleToUser("user-1", role1)

        then:
        result == 2
        and:
        teamRoleService.getRoleForMembership("team-1", "user-1") == role1
        teamRoleService.getRoleForMembership("team-2", "user-1") == role1
    }

    def "validateDataExists should not throw exception when teamMemberRoleData is not empty"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "role-1"]]

        when:
        teamRoleService.validateDataExists()

        then:
        noExceptionThrown()
    }

    def "validateDataExists should throw IllegalStateException when teamMemberRoleData is empty"() {
        given:
        teamRoleService.teamMemberRoleData = [:]

        when:
        teamRoleService.validateDataExists()

        then:
        thrown(IllegalStateException)
    }

    def "validateRoleExists should not throw exception if the role exists in roles"() {
        given:
        teamRoleService.addRole("role-1")

        when:
        teamRoleService.validateRoleExists("role-1")

        then:
        noExceptionThrown()
    }

    def "validateRoleExists should throw exception if the role does not exist in roles"() {
        when:
        teamRoleService.validateRoleExists("role-3")

        then:
        thrown(NoSuchElementException)
    }

    def "validateTeamExists should not throw exception if the given team exists in the teamMemberRoleData map"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE]]

        when:
        teamRoleService.validateTeamExists("team-1")

        then:
        noExceptionThrown()
    }

    def "validateTeamExists should throw exception if the given team does not exist in the teamMemberRoleData map"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE]]

        when:
        teamRoleService.validateTeamExists("team-5")

        then:
        thrown(NoSuchElementException)
    }

    def "validateUserExists should not throw exception if the user exists in some team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE], "team-2": ["user-2": "some-role"]]

        when:
        teamRoleService.validateUserExists("user-1")

        then:
        noExceptionThrown()
    }

    def "validateUserExists should throw exception if the user does not exist in any team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "some-role"]]

        when:
        teamRoleService.validateUserExists("user-2")

        then:
        thrown(NoSuchElementException)
    }

    def "validateUserInTeam should not throw exception if the given team membership exists"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE], "team-2": ["user-2": "some-role"]]

        when:
        teamRoleService.validateUserInTeam("team-1", "user-1")

        then:
        noExceptionThrown()
    }

    def "validateUserInTeam should return false if the given team membership does not exist"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE], "team-2": ["user-2": "some-role"]]

        when:
        teamRoleService.validateUserInTeam("team-1", "user-2")

        then:
        thrown(NoSuchElementException)
    }

}
