package andrea_freddi.U5_W3_D5_J.payloads;

// parallelamente a LoginPayload creo LoginDTO che mi serve per restituire il token
// uso Payload per quello che devo ricevere in costruzione e DTO per quello che restituisco

public record LoginDTO(
        String token
) {
}
