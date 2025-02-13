@Service
@RequiredArgsConstructor
public class EventService {

    private final WebClient webClient;

    public Mono<EventResponseDTO> fetchEventById(Long eventId) {
        return webClient.get()
                .uri("/events/{id}", eventId)
                .retrieve()
                .bodyToMono(EventResponseDTO.class)
                .map(event -> {
                    // Optimized mapping logic if needed
                    event.setName(event.getName().toUpperCase()); // Example transformation
                    return event;
                });
    }
}
@Service
@RequiredArgsConstructor
public class EventService {

    private final WebClient webClient;

    public EventResponseDTO fetchEventById(Long eventId) {
        return webClient.get()
                .uri("/events/{id}", eventId)
                .retrieve()
                .bodyToMono(EventResponseDTO.class)
                .map(event -> {
                    // Optimized mapping logic if needed
                    event.setName(event.getName().toUpperCase()); // Example transformation
                    return event;
                })
                .block(); // Blocking call to return DTO directly
    }
                     }
