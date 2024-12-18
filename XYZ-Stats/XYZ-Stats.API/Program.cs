using Microsoft.EntityFrameworkCore;
using XYZ_Stats.Application.Commands;
using XYZ_Stats.Domain.Entitys;
using XYZ_Stats.Infrastructure;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddDbContext<XyzStatsDbContext>(context =>
{
    context.UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking);
    var connectionString = builder.Configuration.GetConnectionString("SqlConnectionString");
    context.UseSqlServer(connectionString);
});

builder.Services.AddMediatR(c => c.RegisterServicesFromAssemblies(typeof(AddEventCommand).Assembly));

var app = builder.Build();

using var scope = app.Services.CreateScope();
var context = scope.ServiceProvider.GetRequiredService<XyzStatsDbContext>();
context.Database.Migrate();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();