using MediatR;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using XYZ_Stats.Application.Commands;
using XYZ_Stats.Infrastructure;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.DependencyInjection;

namespace VivavisTool2.Tests;

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
                .EnableSensitiveDataLogging();
        });

        var testConfigurationBuilder = new ConfigurationBuilder();
        testConfigurationBuilder.AddInMemoryCollection(new Dictionary<string, string>
        {
            { "ConnectionStrings:GOTENBERG", "http://localhost:3000" }
        }!);
        services.AddSingleton<IConfiguration>(testConfigurationBuilder.Build());

        services.AddMemoryCache();

        services.AddMediatR(cfg =>
        {
            cfg.RegisterServicesFromAssembly(typeof(AddEventCommand).Assembly);
        });

        services.AddLogging();

        var serviceProvider = services.BuildServiceProvider();
        Context = serviceProvider.GetRequiredService<XyzStatsDbContext>();
        Mediator = serviceProvider.GetRequiredService<IMediator>();
    }


    public void Dispose()
    {
        Context.Database.EnsureDeleted();
    }
}