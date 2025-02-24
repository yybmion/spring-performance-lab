package performance.lab2.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import performance.lab2.dto.TeamResponseDto;
import performance.lab2.service.TeamService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/api/teams")
    public ResponseEntity<List<TeamResponseDto>> getTeams() {
        List<TeamResponseDto> response = teamService.findTeams();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teams/fetchJoin")
    public ResponseEntity<List<TeamResponseDto>> getTeamsWithMembers() {
        List<TeamResponseDto> response = teamService.findTeamsWithMembers();
        return ResponseEntity.ok(response);
    }
}
