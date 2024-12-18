using MediatR;
using Microsoft.AspNetCore.Mvc;
using XYZ_Stats.Application.Commands;

namespace XYZ_Stats.API.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class EventController : ControllerBase
    {
        private readonly IMediator _mediator;

        public EventController(IMediator mediator)
        {
            _mediator = mediator;
        }

        [HttpPut("AddEvent", Name = "AddEvent")]
        public async Task<IActionResult> AddBasicStats(string type, string? data)
        {
            await _mediator.Send(new AddEventCommand(type, data));
            return Ok();
        }
    }
}
