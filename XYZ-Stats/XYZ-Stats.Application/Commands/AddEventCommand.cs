using MediatR;
using XYZ_Stats.Infrastructure;
using XYZ_Stats.Domain.Entitys;

namespace XYZ_Stats.Application.Commands
{
    public class AddEventCommand : IRequest
    {
        public string Type { get; }
        public string? Data { get; }

        public AddEventCommand(string type, string? data)
        {
            Type = type;
            Data = data;
        }
    }

    public class AddEventCommandHandler : IRequestHandler<AddEventCommand>
    {
        private readonly XyzStatsDbContext _context;

        public AddEventCommandHandler(XyzStatsDbContext context)
        {
            _context = context;
        }

        public async Task Handle(AddEventCommand request, CancellationToken cancellationToken)
        {
            var loggEvent = new Event()
            {
                EventType = request.Type,
                EventData = request.Data,
            };
            _context.Event.Add(loggEvent);
            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}