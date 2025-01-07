using XYZ_Stats.Application.Commands;

namespace XYZ_Stats.Tests.Tests;

public class EnventTest() : DatabaseTestCase()
{
    [Theory]
    [InlineData("Register", "User1")]
    [InlineData("Register", "User2")]
    [InlineData("Login", "User1")]
    [InlineData("Register", "User3")]
    [InlineData("Register", "User4")]
    [InlineData("Login", "User2")]
    [InlineData("Login", "User3")]
    public async Task AddEventTest(string type, string data)
    {
        string[] array1 = ["apple", "cat", "dog", "house", "tree", "car", "pen", "book", "table", "chair"];
        string[] array2 = ["mouse", "phone", "paper", "pencil", "clock", "door", "window", "bed", "desk", "computer"];

        for (int i = 0; i < 10; i++)
        {
            string str1 = array1[i];
            string str2 = array2[i];
            await Mediator.Send(new AddEventCommand(str1, str2));
        }

        await Mediator.Send(new AddEventCommand(type, data));

        var eventData = Context.Event.Select(e => e.EventData).ToArray();
        var eventTypes = Context.Event.Select(e => e.EventType).ToArray();

        Assert.True(eventData.Length == 11);
        Assert.True(eventTypes.Length == 11);
        Assert.Contains(data, eventData);
        Assert.Contains(type, eventTypes);
    }
}