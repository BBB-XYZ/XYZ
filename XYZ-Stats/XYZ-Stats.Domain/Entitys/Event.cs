using XYZ_Stats.Domain.Entitys.Shared;

namespace XYZ_Stats.Domain.Entitys;

public class Event : EntityBase
{
    public required string EventType { get; set; }
    public string? EventData { get; set; }
}