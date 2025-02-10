using Microsoft.AspNetCore.Mvc;
using StockApi.Data;
using StockApi.Dto;

namespace StockApi.Controllers
{
    [ApiController]
    [Route("project")]
    public class ProjectController : ControllerBase
    {
        private readonly StockContext _context;

        public ProjectController(StockContext context) 
        {
            _context = context;
        }

        [HttpPost("create")]
        public IActionResult Create(CreateProjectRequest request) 
        {
            return Ok(_context.CreateProject(request.Name, request.MasterUrl, request.MappingUrl));
        }

        [HttpGet("info-list")]
        public IActionResult InfoList()
        {
            return Ok(_context.GetProjectInfoList());
        }

        [HttpGet("state-list")]
        public IActionResult StateList() 
        {
            return Ok(_context.GetProjectStateList());
        }

        [HttpGet("state")]
        public IActionResult State(string projectName)
        {
            return Ok(_context.GetProjectState(projectName));
        }



    }
}
