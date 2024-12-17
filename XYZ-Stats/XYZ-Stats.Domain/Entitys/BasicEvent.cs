using XYZ_Stats.Domain.Entitys.Shared;

namespace XYZ_Stats.Domain.Entitys;

public class BasicEvent : EntityBase
{
    public EventType EventType { get; set; }
    public string? EventData { get; set; }
}