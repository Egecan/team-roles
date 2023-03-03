package com.ege.teams.service

import com.ege.teams.common.Constants
import com.ege.teams.model.TeamDetailsResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject


class TeamRoleServiceSpec extends Specification {

    @Subject
    TeamRoleService teamRoleService = new TeamRoleService()

    @Shared
    def DEF_ROLE = "Def Role"


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
        def roleName = "new-role"
        def teamRoleService = new TeamRoleService()
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "old-role"]]

        when:
        teamRoleService.assignRoleToMembership("team-1", "user-1", roleName)

        then:
        teamRoleService.teamMemberRoleData["team-1"]["user-1"] == roleName

    }

    def "should not update the role when team is not found"() {
        given:
        def roleName = "new-role"
        def teamRoleService = new TeamRoleService()
        teamRoleService.teamMemberRoleData = [:]

        when:
        teamRoleService.assignRoleToMembership("team-1", "user-1", roleName)

        then:
        teamRoleService.teamMemberRoleData["team-1"] == null

    }

    def "getRoleForMembership should return the correct role for a given user in a given team"() {
        given:
        def roleName = "new-role"
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": roleName]]
        print(teamRoleService.teamMemberRoleData)

        when:
        def result = teamRoleService.getRoleForMembership("team-1", "user-1")

        then:
        result == roleName
    }

    def "getRoleForMembership should return null when the given user is not a member of the given team"() {
        given:
        def roleName = "new-role"
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": roleName]]

        when:
        def result = teamRoleService.getRoleForMembership("team-1", "user-2")

        then:
        result == null
    }

    def "getRoleForMembership should return null when the given team does not exist in the data"() {
        given:
        teamRoleService.teamMemberRoleData = [:]

        when:
        def result = teamRoleService.getRoleForMembership("team-1", "user-1")

        then:
        result == null
    }

    def "getMembershipsForRole should return an empty list when no team memberships exist"() {
        given:
        teamRoleService.teamMemberRoleData = [:]

        when:
        def result = teamRoleService.getMembershipsForRole("new-role")

        then:
        result.isEmpty()
    }

    def "getMembershipsForRole should return an empty list when no members have the specified role"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE, "user-2": DEF_ROLE]]

        when:
        def result = teamRoleService.getMembershipsForRole("new-role")

        then:
        result.isEmpty()
    }

    def "getMembershipsForRole should return memberships for a single team when a single team has members with the role"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "new-role", "user-2": DEF_ROLE]]

        when:
        def result = teamRoleService.getMembershipsForRole("new-role")

        then:
        result.size() == 1
        result.get(0).teamId == "team-1"
        result.get(0).userId == "user-1"
    }

    def "getMembershipsForRole should return memberships for multiple teams when multiple teams have members with the specified role"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "new-role", "user-2": DEF_ROLE],
                                              "team-2": ["user-3": "new-role", "user-4": DEF_ROLE]]

        when:
        def result = teamRoleService.getMembershipsForRole("new-role")

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

    def "doesTeamExistInTeamMemberships should return true if the given team exists in the teamMemberRoleData map"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE]]

        when:
        def result = teamRoleService.doesTeamExistInTeamMemberships("team-1")

        then:
        result
    }

    def "getTeamMembers should return the correct team members for the given team"() {
        given:
        def members = ["user-1": "role-1", "user-2": "role-2"]
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
        def roleName = "new-role"
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE], "team-2": ["user-1": DEF_ROLE]]

        when:
        def result = teamRoleService.assignRoleToUser("user-1", roleName)

        then:
        result == 2
        and:
        teamRoleService.getRoleForMembership("team-1", "user-1") == roleName
        teamRoleService.getRoleForMembership("team-2", "user-1") == roleName
    }

    def "doesUserExistInAnyTeam should return true if the user exists in any team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": DEF_ROLE], "team-2": ["user-2": "some-role"]]

        when:
        def result = teamRoleService.doesUserExistInAnyTeam("user-1")

        then:
        result
    }

    def "doesUserExistInAnyTeam should return false if the user does not exist in any team"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "some-role"]]

        when:
        def result = teamRoleService.doesUserExistInAnyTeam("user-2")

        then:
        !result
    }

    def "checkDataExistsInTeamMemberships should return true when teamMemberRoleData is not empty"() {
        given:
        teamRoleService.teamMemberRoleData = ["team-1": ["user-1": "role-1"]]

        when:
        def result = teamRoleService.checkDataExistsInTeamMemberships()

        then:
        result
    }

    def "checkDataExistsInTeamMemberships should return false when teamMemberRoleData is empty"() {
        given:
        teamRoleService.teamMemberRoleData = [:]

        when:
        def result = teamRoleService.checkDataExistsInTeamMemberships()

        then:
        !result
    }

}
