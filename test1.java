@Service
@RequiredArgsConstructor
public class EventService {

    private final WebClient webClient;

    public EventResponseDTO fetchEventById(Long eventId) {
        try {
            return webClient.get()
                    .uri("/events/{id}", eventId)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> {
                        return response.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new EventNotFoundException("Event not found: " + errorMessage)));
                    })
                    .onStatus(HttpStatus::is5xxServerError, response -> {
                        return Mono.error(new ServerDownException("External service is unavailable"));
                    })
                    .bodyToMono(EventResponseDTO.class)
                    .block(); // Blocking call to return DTO directly
        } catch (WebClientResponseException.NotFound e) {
            throw new EventNotFoundException("Event with ID " + eventId + " not found");
        } catch (WebClientRequestException e) {
            throw new ServerDownException("Failed to connect to external service");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }
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
