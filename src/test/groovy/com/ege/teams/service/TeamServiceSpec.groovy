package com.ege.teams.service

import com.ege.teams.common.Constants
import com.ege.teams.model.TeamDetailsResponse
import com.ege.teams.model.TeamsResponse
import org.springframework.web.client.RestTemplate
import spock.lang.Specification


class TeamServiceSpec extends Specification {
    RestTemplate restTemplate = Mock(RestTemplate)

    TeamService teamService = new TeamService(restTemplate)

    def "getTeam should return team details for given team id"() {
        given:
        def teamId = "team-1"
        def teamDetails = new TeamDetailsResponse(teamId,"random-team","some-id", ["user-1", "user-2", "user-3"])
        restTemplate.getForObject("$Constants.GET_TEAMS_ENDPOINT/$teamId", TeamDetailsResponse.class) >> teamDetails

        when:
        def result = teamService.getTeam(teamId)

        then:
        result == teamDetails
    }

    def "should return all teams"() {
        given:
        def expectedTeams = new TeamsResponse[2]
        expectedTeams[0] = new TeamsResponse("team-1","random-team")
        expectedTeams[1] = new TeamsResponse( "team-2", "awesome-team")
        restTemplate.getForObject("$Constants.GET_TEAMS_ENDPOINT", _) >> expectedTeams

        when:
        def teams = teamService.getAllTeams()

        then:
        teams == expectedTeams
    }
}
