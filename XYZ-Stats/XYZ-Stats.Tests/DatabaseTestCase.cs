using MediatR;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using XYZ_Stats.Application.Commands;
using XYZ_Stats.Infrastructure;

namespace XYZ_Stats.Tests;

public class DatabaseTestCase : IDisposable
{
    public XyzStatsDbContext Context { get; }
    public IMediator Mediator { get; }

    public DatabaseTestCase()
    {
        var id = Guid.NewGuid().ToString().Replace("-", "");

        var databaseName = $"xyz_stats_dev_{id}";

        var services = new ServiceCollection();

        // services.AddHttpContextAccessor();
        services.AddDbContext<XyzStatsDbContext>(option =>
        {
            option.UseQueryTrackingBehavior(queryTrackingBehavior: QueryTrackingBehavior.NoTracking);
            option.UseSqlServer(
                    $"Server=127.0.0.1,1444;" +
                                $"Initial Catalog={databaseName};" +
                                $"User ID=sa;" +
                                $"Password=MySecurePassword007!;" +
                                $"Integrated Security=false;" +
                                $"TrustServerCertificate=True;"
                    )
                .EnableSensitiveDataLogging()
                .EnableDetailedErrors();
        });

        services.AddMemoryCache();

        services.AddMediatR(cfg =>
        {
            cfg.RegisterServicesFromAssembly(typeof(AddEventCommand).Assembly);
        });

        services.AddLogging();

        var serviceProvider = services.BuildServiceProvider();
        Context = serviceProvider.GetRequiredService<XyzStatsDbContext>();
        Mediator = serviceProvider.GetRequiredService<IMediator>();

        Context.Database.EnsureCreated();
    }


    public void Dispose()
    {
        Context.Database.EnsureDeleted();
    }
}