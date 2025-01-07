using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using XYZ_Stats.Domain.Entitys;

namespace XYZ_Stats.Infrastructure.Configurations;

public class EventConfiguration : IEntityTypeConfiguration<Event>
{
    public void Configure(EntityTypeBuilder<Event> builder)
    {
        builder.Property(e => e.EventType)
            .HasMaxLength(255);

        builder.Property(e => e.EventData)
            .HasMaxLength(255);
    }
}