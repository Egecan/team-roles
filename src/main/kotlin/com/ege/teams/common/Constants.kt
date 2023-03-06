package com.ege.teams.common

object Constants {
    const val GET_TEAMS_ENDPOINT = "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams"

    const val DEFAULT_ROLE = "Developer"
    const val PO_ROLE = "Product Owner"
    const val TESTER_ROLE = "Tester"

    const val ROLE_ASSIGNED = "Role %s assigned to user %s in team %s"
    const val ROLE_ASSIGNED_USER = "Role %s assigned to user %s in %s team(s)"
    const val ROLE_ADDED = "Role %s added to roles"
    const val DATA_RELOADED = "Reloaded teams data"

    const val INVALID_ROLE = "Invalid role"
    const val TEAM_NOT_FOUND = "Team not found"
    const val USER_NOT_FOUND = "User not found in team"
    const val USER_NOT_FOUND_ANYWHERE = "User not found in team"
    const val DATA_NOT_FOUND = "No teams data found. Please try reloading data"
    const val GENERIC_ERROR_MESSAGE = "Unable to process request"
}